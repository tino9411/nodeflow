package com.nodeflow.connections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeRegistry;

public class ConnectionManager {
    private static final Map<String, Set<Connection>> connections = new HashMap<>();
    
    public static void createConnection(String sourceNodeId, String targetNodeId) {
        Connection connection = new Connection(sourceNodeId, targetNodeId);
        
        // Update the nodes
        Node sourceNode = NodeRegistry.findNodeById(sourceNodeId);
        Node targetNode = NodeRegistry.findNodeById(targetNodeId);
        
        if (sourceNode != null && targetNode != null) {
            sourceNode.addOutputTarget(targetNodeId);
            targetNode.addInputSource(sourceNodeId);
            
            // Store the connection
            connections.computeIfAbsent(sourceNodeId, k -> new HashSet<>()).add(connection);
        }
    }
    
    public static void removeConnection(String sourceNodeId, String targetNodeId) {
        Set<Connection> sourceConnections = connections.get(sourceNodeId);
        if (sourceConnections != null) {
            sourceConnections.removeIf(conn -> 
                conn.getTargetNodeId().equals(targetNodeId));
            
            // Update the nodes
            Node sourceNode = NodeRegistry.findNodeById(sourceNodeId);
            Node targetNode = NodeRegistry.findNodeById(targetNodeId);
            
            if (sourceNode != null && targetNode != null) {
                sourceNode.removeOutputTarget(targetNodeId);
                targetNode.removeInputSource(sourceNodeId);
            }
        }
    }
    
    public static Set<Connection> getNodeConnections(String nodeId) {
        return connections.getOrDefault(nodeId, new HashSet<>());
    }
}