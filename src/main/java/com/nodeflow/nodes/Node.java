package com.nodeflow.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nodeflow.behaviours.NodeBehaviour;
import com.nodeflow.status.Status;

public class Node {

  private final String nodeId;
  private NodeType nodeType;
  private Status status;
  private String inputData;
  private String output;
	private  final List<NodeBehaviour> behaviours;

  public Node(NodeType nodeType) {
    this.nodeId = UUID.randomUUID().toString();
    this.nodeType = nodeType;
    this.status = Status.PENDING;
		this.behaviours = new ArrayList<>();
  }

  public Node(String nodeId, NodeType nodeType) {
    this.nodeId = nodeId;
    this.nodeType = nodeType;
    this.status = Status.PENDING;
    this.behaviours = new ArrayList<>();
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

	public void updateStatus(Status newStatus) {
		this.status = newStatus;
		logAction("Status updated to " + newStatus.toString());
	}

	public void addBehaviour(NodeBehaviour behaviour) {
		behaviours.add(behaviour);
	}

	public void run() {
		updateStatus(Status.RUNNING);
		for (NodeBehaviour behaviour : behaviours) {
			behaviour.execute(this);
		}
		updateStatus(Status.COMPLETED);
	}

	private void logAction(String message) {
		System.out.println(message);
	}
}
