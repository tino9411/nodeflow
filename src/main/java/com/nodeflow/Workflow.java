package com.nodeflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.nodeflow.nodes.Node;

public class Workflow {
    private final List<Node> nodes;
    private final Map<String, Node> nodeMap; // Map for quick lookup by nodeId
    private final Set<String> completedNodes; // Track completed node IDs
    private final Queue<Node> readyNodes; // Queue of nodes ready to run

    public Workflow(List<Node> nodes) {
        this.nodes = nodes;
        this.nodeMap = new HashMap<>();
        this.completedNodes = new HashSet<>();
        this.readyNodes = new LinkedList<>();
        
        for (Node node : nodes) {
            nodeMap.put(node.getNodeId(), node);
            if (node.getInputSources().isEmpty()) {
                readyNodes.add(node); // Add nodes with no dependencies to ready queue
            }
        }
    }

    public void execute() {
        while (!readyNodes.isEmpty()) {
            Node node = readyNodes.poll();
            node.run();
            completedNodes.add(node.getNodeId());

            // Check output targets to see if they are now ready to run
            for (String targetId : node.getOutputTargets()) {
                Node targetNode = nodeMap.get(targetId);
                if (targetNode != null && areDependenciesMet(targetNode)) {
                    readyNodes.add(targetNode);
                }
            }
        }
    }

    private boolean areDependenciesMet(Node node) {
        for (String inputSourceId : node.getInputSources()) {
            if (!completedNodes.contains(inputSourceId)) {
                return false;
            }
        }
        return true;
    }
}