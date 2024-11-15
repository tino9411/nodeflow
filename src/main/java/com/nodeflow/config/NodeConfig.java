package com.nodeflow.config;

import java.util.List;

public class NodeConfig {
    private String nodeId;
    private String nodeType;
    private List<BehaviourConfig> behaviours;
    private List<ConnectionConfig> connections;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public List<BehaviourConfig> getBehaviours() {
        return behaviours;
    }

    public void setBehaviours(List<BehaviourConfig> behaviours) {
        this.behaviours = behaviours;
    }

    public List<ConnectionConfig> getConnections() {
        return connections;
    }

    public void setConnections(List<ConnectionConfig> connections) {
        this.connections = connections;
    }
}