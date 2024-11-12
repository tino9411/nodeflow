package com.nodeflow.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nodeflow.behaviours.NodeBehaviour;
import com.nodeflow.status.Status;

public class Node {
    private static final Logger logger = LoggerFactory.getLogger(Node.class);
    
    private final String nodeId;
    private NodeType nodeType;
    private Status status;
    private String inputData;
    private String output;
    private final List<NodeBehaviour> behaviours;
    private final Set<String> inputSources;
    private final Set<String> outputTargets;

    public Node(NodeType nodeType) {
        this.nodeId = UUID.randomUUID().toString();
        this.nodeType = nodeType;
        this.status = Status.PENDING;
        this.behaviours = new ArrayList<>();
        this.inputSources = new HashSet<>();
        this.outputTargets = new HashSet<>();
        logger.info("Created new node [id={}] of type [{}]", this.nodeId, this.nodeType);
    }

    public Node(String nodeId, NodeType nodeType) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.status = Status.PENDING;
        this.behaviours = new ArrayList<>();
        this.inputSources = new HashSet<>();
        this.outputTargets = new HashSet<>();
        logger.info("Created new node with specified id [id={}] of type [{}]", this.nodeId, this.nodeType);
    }

    public String getNodeId() {
        return nodeId;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        logger.debug("Setting node type [id={}] from [{}] to [{}]", nodeId, this.nodeType, nodeType);
        this.nodeType = nodeType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        logger.debug("Setting status [id={}] from [{}] to [{}]", nodeId, this.status, status);
        this.status = status;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        logger.debug("Setting input data [id={}] to [{}]", nodeId, inputData);
        this.inputData = inputData;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        logger.debug("Setting output [id={}] to [{}]", nodeId, output);
        this.output = output;
    }

    public void updateStatus(Status newStatus) {
        logger.debug("Updating status [id={}] from [{}] to [{}]", nodeId, this.status, newStatus);
        this.status = newStatus;
    }

    public void addBehaviour(NodeBehaviour behaviour) {
        logger.debug("Adding behaviour [id={}] of type [{}]", nodeId, behaviour.getClass().getSimpleName());
        behaviours.add(behaviour);
    }

    public Set<String> getInputSources() {
        return new HashSet<>(inputSources);
    }

    public Set<String> getOutputTargets() {
        return new HashSet<>(outputTargets);
    }

    public void addInputSource(String sourceNodeId) {
        logger.debug("Adding input source [node_id={}] [source_id={}]", nodeId, sourceNodeId);
        inputSources.add(sourceNodeId);
    }

    public void addOutputTarget(String targetNodeId) {
        logger.debug("Adding output target [node_id={}] [target_id={}]", nodeId, targetNodeId);
        outputTargets.add(targetNodeId);
    }

    public void removeInputSource(String sourceNodeId) {
        logger.debug("Removing input source [node_id={}] [source_id={}]", nodeId, sourceNodeId);
        inputSources.remove(sourceNodeId);
    }

    public void removeOutputTarget(String targetNodeId) {
        logger.debug("Removing output target [node_id={}] [target_id={}]", nodeId, targetNodeId);
        outputTargets.remove(targetNodeId);
    }

    public void run() {
        logger.info("Starting node execution [id={}]", nodeId);
        try {
            updateStatus(Status.RUNNING);
            
            for (NodeBehaviour behaviour : behaviours) {
                String behaviourName = behaviour.getClass().getSimpleName();
                logger.debug("Executing behaviour [id={}] [behaviour={}]", nodeId, behaviourName);
                
                try {
                    behaviour.execute(this);
                    logger.debug("Successfully executed behaviour [id={}] [behaviour={}]", nodeId, behaviourName);
                } catch (Exception e) {
                    logger.error("Error executing behaviour [id={}] [behaviour={}]", nodeId, behaviourName, e);
                    updateStatus(Status.ERROR);
                    throw e;
                }
            }
            
            updateStatus(Status.COMPLETED);
            logger.info("Completed node execution [id={}]", nodeId);
        } catch (Exception e) {
            logger.error("Failed to execute node [id={}]", nodeId, e);
            updateStatus(Status.ERROR);
            throw e;
        }
    }
}