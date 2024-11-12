package com.nodeflow.connections;

public class Connection {
    private final String sourceNodeId;
    private final String targetNodeId;
    private ConnectionType type;
    
    public Connection(String sourceNodeId, String targetNodeId) {
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.type = ConnectionType.DIRECT;
    }
    
    public String getSourceNodeId() {
        return sourceNodeId;
    }
    
    public String getTargetNodeId() {
        return targetNodeId;
    }
    
    public ConnectionType getType() {
        return type;
    }
    
    public void setType(ConnectionType type) {
        this.type = type;
    }
}