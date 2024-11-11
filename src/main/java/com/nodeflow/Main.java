package com.nodeflow;

import java.util.List;
import java.util.Map;

import com.nodeflow.builder.NodeBuilder;
import com.nodeflow.config.BehaviourConfig;
import com.nodeflow.config.NodeConfig;
import com.nodeflow.config.YAMLConfigLoader;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeType;

public class Main {
    public static void main(String[] args) {
        // Load node configurations from the YAML file
        List<NodeConfig> nodeConfigs = YAMLConfigLoader.loadConfig("config.yaml");

        // Create nodes based on loaded configurations
        for (NodeConfig nodeConfig : nodeConfigs) {
            NodeBuilder nodeBuilder = new NodeBuilder()
                    .setNodeId(nodeConfig.getNodeId())
                    .setNodeType(NodeType.valueOf(nodeConfig.getNodeType()));

            // Add each behavior from the config to the builder
            for (BehaviourConfig behaviour : nodeConfig.getBehaviours()) {
                nodeBuilder.addBehaviour(behaviour.getType(), behaviour.getConfig());
            }

            // Build the node and execute it
            Node node = nodeBuilder.build();
            node.run();
        }

        // Example programmatic creation of a node with custom behavior configuration
        Map<String, Object> customConfig = Map.of("sourceType", "Database", "connectionString", "jdbc:mysql://localhost:3306/mydb" ); // Define custom configuration
        Node dynamicNode = new NodeBuilder()
                .setNodeId("node5")
                .setNodeType(NodeType.DATA_FETCH)
                .addBehaviour("DataFetcher", customConfig)
                .build();
        dynamicNode.run();
    }
}