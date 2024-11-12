package com.nodeflow.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.nodeflow.behaviours.NodeBehaviour;
import com.nodeflow.status.Status;

public class Node {

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
    logAction("Created node with ID: " + this.nodeId + " and type: " + this.nodeType);
  }

  public Node(String nodeId, NodeType nodeType) {
    this.nodeId = nodeId;
    this.nodeType = nodeType;
    this.status = Status.PENDING;
    this.behaviours = new ArrayList<>();
    this.inputSources = new HashSet<>();
    this.outputTargets = new HashSet<>();
    logAction("Created node with ID: " + this.nodeId + " and type: " + this.nodeType);
  }

  public String getNodeId() {
    return nodeId;
  }

  public NodeType getNodeType() {
    return nodeType;
  }

  public void setNodeType(NodeType nodeType) {
    this.nodeType = nodeType;
    logAction("Node type set to: " + nodeType);
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
    logAction("Status set to: " + status);
  }

  public String getInputData() {
    return inputData;
  }

  public void setInputData(String inputData) {
    this.inputData = inputData;
    logAction("Input data set to: " + inputData);
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
    logAction("Output set to: " + output);
  }

  public void updateStatus(Status newStatus) {
    this.status = newStatus;
    logAction("Status updated to " + newStatus);
  }

  public void addBehaviour(NodeBehaviour behaviour) {
    behaviours.add(behaviour);
    logAction("Added behaviour: " + behaviour.getClass().getSimpleName());
  }

  public Set<String> getInputSources() {
    return new HashSet<>(inputSources);
  }

  public Set<String> getOutputTargets() {
      return new HashSet<>(outputTargets);
  }

  public void addInputSource(String sourceNodeId) {
      inputSources.add(sourceNodeId);
  }

  public void addOutputTarget(String targetNodeId) {
      outputTargets.add(targetNodeId);
  }

  public void removeInputSource(String sourceNodeId) {
      inputSources.remove(sourceNodeId);
  }

  public void removeOutputTarget(String targetNodeId) {
      outputTargets.remove(targetNodeId);
  }



  public void run() {
    logAction("Running node with ID: " + nodeId);
    updateStatus(Status.RUNNING);

    for (NodeBehaviour behaviour : behaviours) {
      logAction("Executing behaviour: " + behaviour.getClass().getSimpleName());
      behaviour.execute(this);
    }

    updateStatus(Status.COMPLETED);
    logAction("Completed execution for node with ID: " + nodeId);
  }

  private void logAction(String message) {
    System.out.println("Node [" + nodeId + "]: " + message);
  }
}