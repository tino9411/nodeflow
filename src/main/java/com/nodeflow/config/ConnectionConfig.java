package com.nodeflow.config;

public class ConnectionConfig {
    private String sourceNodeId;
    private String targetNodeId;
    private String type;
    
    public String getSourceNodeId() {
        return sourceNodeId;
    }
    
    public void setSourceNodeId(String sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }
    
    public String getTargetNodeId() {
        return targetNodeId;
    }
    
    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}