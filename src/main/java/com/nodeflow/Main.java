package com.nodeflow;

import java.util.List;

import com.nodeflow.assembly.NodeAssembler;
import com.nodeflow.nodes.Node;

public class Main {
    public static void main(String[] args) {
        NodeAssembler assembler = new NodeAssembler();

        // Create nodes from YAML configuration
        List<Node> nodesFromConfig = assembler.createNodesFromConfig("config.yaml");

        // Initialize and execute the workflow
        Workflow workflow = new Workflow(nodesFromConfig, 3);
        workflow.execute();
    }
}