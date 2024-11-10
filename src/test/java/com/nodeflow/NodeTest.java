package com.nodeflow;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeType;
import com.nodeflow.status.Status;

class TestNode extends Node {
  protected TestNode(NodeType nodeType) {
    super(nodeType);
  }

  @Override
  public void run() {
    setOutput("Test output");
  }
}

public class NodeTest {

  @Test
  public void testNodeIdUniqueness() {
    Node node1 = new TestNode(NodeType.DATA_FETCH);
    Node node2 = new TestNode(NodeType.DATA_FETCH);

    assertNotNull(node1.getNodeId());
    assertNotNull(node2.getNodeId());
    assertNotEquals(node1.getNodeId(), node2.getNodeId(), "Node IDs should be unique");
  }

  @Test
  public void testDefaultStatus() {
    Node node = new TestNode(NodeType.DATA_FETCH);
    assertEquals(Status.PENDING, node.getStatus(), "Default status should be PENDING");
  }

  @Test
  public void testUpdateStatus() {
    Node node = new TestNode(NodeType.DATA_FETCH);
    node.updateStatus(Status.RUNNING);
    assertEquals(Status.RUNNING, node.getStatus(), "Status should be update to RUNNING");
  }

  @Test
  public void testInputOutputData() {
    Node node = new TestNode(NodeType.DATA_FETCH);
    node.setInputData("Test input");
    node.setOutput("Test output");

    assertEquals("Test input", node.getInputData(), "Input data should match");
    assertEquals("Test output", node.getOutput(), "Output data should match");
  }
}
