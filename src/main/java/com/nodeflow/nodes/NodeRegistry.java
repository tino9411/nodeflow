package com.nodeflow.nodes;

import java.util.HashMap;
import java.util.Map;

public class NodeRegistry {

    private static final Map<String, Node> nodes = new HashMap<>();

    public static void registerNode(Node node) {
        nodes.put(node.getNodeId(), node);
        System.out.println("Registered node with ID: " + node.getNodeId()); // Debug log
    }

    public static Node findNodeById(String nodeId) {
        return nodes.get(nodeId);
    }

    public static void removeNode(String nodeId) {
        nodes.remove(nodeId);
    }
    
}
