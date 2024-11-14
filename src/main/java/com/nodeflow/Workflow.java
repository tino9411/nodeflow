package com.nodeflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nodeflow.nodes.Node;

public class Workflow {
    private static final Logger logger = LoggerFactory.getLogger(Workflow.class);
    private static final long DEFAULT_TIMEOUT_MINUTES = 60;

    private final List<Node> nodes;
    private final Map<String, Node> nodeMap;
    private final Set<String> completedNodes;
    private final BlockingQueue<Node> readyNodes;
    private final ExecutorService executorService;
    private final int maxParallelism;
    private final AtomicInteger activeNodeCount;
    private volatile boolean isShutdown;
    private volatile Throwable executionError;

    public Workflow(List<Node> nodes, int maxParallelism) {
        logger.info("Initializing workflow with {} nodes and parallelism of {}", nodes.size(), maxParallelism);
        
        this.nodes = new ArrayList<>(Objects.requireNonNull(nodes, "Nodes list cannot be null"));
        this.maxParallelism = validateParallelism(maxParallelism);
        this.nodeMap = new ConcurrentHashMap<>();
        this.completedNodes = Collections.synchronizedSet(new HashSet<>());
        this.readyNodes = new LinkedBlockingQueue<>();
        this.activeNodeCount = new AtomicInteger(0);
        this.executorService = createExecutorService();
        
        initializeWorkflow();
    }

    private int validateParallelism(int maxParallelism) {
        if (maxParallelism < 1) {
            logger.warn("Invalid parallelism value: {}. Setting to 1", maxParallelism);
            return 1;
        }
        return maxParallelism;
    }

    private ExecutorService createExecutorService() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadCount = new AtomicInteger(1);
            
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "workflow-executor-" + threadCount.getAndIncrement());
                thread.setUncaughtExceptionHandler((t, e) -> handleUncaughtException(e));
                return thread;
            }
        };
        
        return new ThreadPoolExecutor(
            maxParallelism, maxParallelism,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    private void initializeWorkflow() {
        logger.debug("Starting workflow initialization");
        
        try {
            for (Node node : nodes) {
                String nodeId = node.getNodeId();
                if (nodeMap.putIfAbsent(nodeId, node) != null) {
                    throw new WorkflowExecutionException("Duplicate node ID detected: " + nodeId);
                }

                Set<String> inputSources = node.getInputSources();
                logger.debug("Processing node [id={}] with {} input dependencies", nodeId, inputSources.size());

                if (inputSources.isEmpty()) {
                    readyNodes.add(node);
                    logger.debug("Added node [id={}] to ready queue (no dependencies)", nodeId);
                } else {
                    validateNodeDependencies(node);
                    logger.debug("Node [id={}] has dependencies: {}", nodeId,
                            inputSources.stream().collect(Collectors.joining(", ")));
                }
            }

            logger.info("Workflow initialization completed. {} nodes ready for execution", readyNodes.size());
        } catch (Exception e) {
            throw new WorkflowExecutionException("Failed to initialize workflow", e);
        }
    }

    private void validateNodeDependencies(Node node) {
        for (String inputId : node.getInputSources()) {
            if (!nodeMap.containsKey(inputId)) {
                throw new WorkflowExecutionException(
                    String.format("Node [id=%s] depends on non-existent node [id=%s]", 
                        node.getNodeId(), inputId));
            }
        }
    }

    public void execute() {
        execute(DEFAULT_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    public void execute(long timeout, TimeUnit timeUnit) {
        logger.info("Starting workflow execution with timeout of {} {}", timeout, timeUnit);
        long startTime = System.currentTimeMillis();

        try {
            startNodeExecution();
            boolean completed = waitForCompletion(timeout, timeUnit);
            
            if (!completed) {
                handleTimeout();
            }

            if (executionError != null) {
                throw new WorkflowExecutionException("Workflow execution failed", executionError);
            }

            verifyWorkflowCompletion();

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Workflow execution completed successfully in {} ms", duration);

        } catch (Exception e) {
            logger.error("Workflow execution failed", e);
            throw new WorkflowExecutionException("Workflow execution failed", e);
        } finally {
            shutdown();
        }
    }

    private void startNodeExecution() {
        CompletableFuture.runAsync(() -> {
            try {
                while (!isShutdown && (executionError == null)) {
                    Node node = readyNodes.poll(100, TimeUnit.MILLISECONDS);
                    if (node != null) {
                        scheduleNode(node);
                    } else if (activeNodeCount.get() == 0 && readyNodes.isEmpty()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                handleUncaughtException(e);
            }
        }, executorService);
    }

    private void scheduleNode(Node node) {
        activeNodeCount.incrementAndGet();
        CompletableFuture.runAsync(() -> {
            String nodeId = node.getNodeId();
            try {
                logger.debug("Executing node [id={}]", nodeId);
                node.run();
                completedNodes.add(nodeId);
                processOutputTargets(node);
            } catch (Exception e) {
                handleUncaughtException(e);
            } finally {
                activeNodeCount.decrementAndGet();
            }
        }, executorService);
    }

    private void processOutputTargets(Node node) {
        Set<String> outputTargets = node.getOutputTargets();
        logger.debug("Processing {} output targets for node [id={}]", 
            outputTargets.size(), node.getNodeId());

        for (String targetId : outputTargets) {
            Node targetNode = nodeMap.get(targetId);
            if (targetNode == null) {
                logger.warn("Output target node not found [id={}]", targetId);
                continue;
            }

            if (areDependenciesMet(targetNode)) {
                readyNodes.offer(targetNode);
                logger.debug("Added node [id={}] to ready queue (all dependencies met)", targetId);
            }
        }
    }

    private boolean areDependenciesMet(Node node) {
        return node.getInputSources().stream()
                .allMatch(completedNodes::contains);
    }

    private boolean waitForCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        
        while (System.currentTimeMillis() < endTime) {
            if (executionError != null) {
                return true;
            }
            
            if (activeNodeCount.get() == 0 && readyNodes.isEmpty()) {
                return true;
            }
            
            Thread.sleep(100);
        }
        
        return false;
    }

    private void handleTimeout() {
        String message = String.format("Workflow execution timed out. Active nodes: %d, Ready nodes: %d", 
            activeNodeCount.get(), readyNodes.size());
        logger.error(message);
        throw new WorkflowExecutionException(message);
    }

    private void handleUncaughtException(Throwable e) {
        executionError = e;
        shutdown();
    }

    private void verifyWorkflowCompletion() {
        Set<String> unexecutedNodes = nodes.stream()
                .map(Node::getNodeId)
                .filter(id -> !completedNodes.contains(id))
                .collect(Collectors.toSet());

        if (!unexecutedNodes.isEmpty()) {
            String message = String.format("Workflow completed with %d unexecuted nodes: %s", 
                unexecutedNodes.size(), String.join(", ", unexecutedNodes));
            logger.error(message);
            throw new WorkflowExecutionException(message);
        }
    }

    private void shutdown() {
        isShutdown = true;
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                logger.warn("Executor service did not terminate in the specified time");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Shutdown was interrupted", e);
        }
    }

    public static class WorkflowExecutionException extends RuntimeException {
        public WorkflowExecutionException(String message) {
            super(message);
        }

        public WorkflowExecutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}