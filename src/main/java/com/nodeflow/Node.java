package com.nodeflow;

import java.util.UUID;

public abstract class Node {

  private final String nodeId;
  private NodeType nodeType;
  private Status status;
  private String inputData;
  private String output;

  protected Node(NodeType nodeType) {
    this.nodeId = UUID.randomUUID().toString();
    this.nodeType = nodeType;
    this.status = Status.PENDING;
  }

  public String getNodeId() {
    return nodeId;
  }

  public NodeType getNodeType() {
    return nodeType;
  }

  public void setNodeType(NodeType nodeType) {
    this.nodeType = nodeType;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getInputData() {
    return inputData;
  }

  public void setInputData(String inputData) {
    this.inputData= inputData;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

	protected void updateStatus(Status newStatus) {
		this.status = newStatus;
		logAction("Status updated to " + newStatus.toString());
	}

	public abstract void run();

	private void logAction(String message) {
		System.out.println(message);
	}
}
