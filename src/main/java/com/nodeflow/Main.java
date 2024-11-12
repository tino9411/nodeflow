package com.nodeflow;

import java.util.List;
import java.util.Map;

import com.nodeflow.assembly.NodeAssembler;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeType;

public class Main {
    public static void main(String[] args) {
        NodeAssembler assembler = new NodeAssembler();

        // Create nodes from YAML configuration and run them
        List<Node> nodesFromConfig = assembler.createNodesFromConfig("config.yaml");
        for (Node node : nodesFromConfig) {
            node.run();
        }

        // Create a custom node programmatically and run it
        Map<String, Object> customConfig = Map.of("sourceType", "Database", "connectionString", "jdbc:mysql://localhost:3306/mydb");
        Node dynamicNode = assembler.createNode("node6", NodeType.DATA_FETCH, "DataFetcher", customConfig);
        dynamicNode.run();
    }
}