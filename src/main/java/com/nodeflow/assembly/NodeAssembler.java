package com.nodeflow.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nodeflow.config.BehaviourConfig;
import com.nodeflow.config.ConnectionConfig;
import com.nodeflow.config.NodeConfig;
import com.nodeflow.config.YAMLConfigLoader;
import com.nodeflow.connections.ConnectionManager;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeBuilder;
import com.nodeflow.nodes.NodeType;

/**
 * NodeAssembler is responsible for orchestrating the creation of Node instances
 * and their connections. It handles both YAML configuration-based and programmatic
 * node creation.
 */
public class NodeAssembler {

    /**
     * Creates a list of Node instances based on configurations loaded from a YAML file.
     *
     * @param configPath The path to the YAML configuration file
     * @return List of assembled Node instances
     */
    public List<Node> createNodesFromConfig(String configPath) {
        List<NodeConfig> nodeConfigs = YAMLConfigLoader.loadConfig(configPath);
        List<Node> nodes = new ArrayList<>();

        // First create all nodes
        for (NodeConfig nodeConfig : nodeConfigs) {
            NodeBuilder nodeBuilder = new NodeBuilder()
                    .setNodeId(nodeConfig.getNodeId())
                    .setNodeType(NodeType.valueOf(nodeConfig.getNodeType()));

            // Add behaviors
            if (nodeConfig.getBehaviours() != null) {
                for (BehaviourConfig behaviour : nodeConfig.getBehaviours()) {
                    nodeBuilder.addBehaviour(behaviour.getType(), behaviour.getConfig());
                }
            }

            nodes.add(nodeBuilder.build());
        }

        // Then set up connections
        setupConnections(nodeConfigs);

        return nodes;
    }

    /**
     * Creates a single Node instance programmatically.
     *
     * @param nodeId The unique identifier for the node
     * @param nodeType The type of the node
     * @param behaviourType The type of behavior to add
     * @param config Configuration for the behavior
     * @return Assembled Node instance
     */
    public Node createNode(String nodeId, NodeType nodeType, String behaviourType, Map<String, Object> config) {
        return new NodeBuilder()
                .setNodeId(nodeId)
                .setNodeType(nodeType)
                .addBehaviour(behaviourType, config)
                .build();
    }

    /**
     * Creates a connection between two existing nodes.
     *
     * @param sourceNodeId ID of the source node
     * @param targetNodeId ID of the target node
     */
    public void createConnection(String sourceNodeId, String targetNodeId) {
        ConnectionManager.createConnection(sourceNodeId, targetNodeId);
    }

    /**
     * Sets up connections between nodes based on their configuration.
     *
     * @param nodeConfigs List of node configurations
     */
    private void setupConnections(List<NodeConfig> nodeConfigs) {
        for (NodeConfig nodeConfig : nodeConfigs) {
            if (nodeConfig.getConnections() != null) {
                for (ConnectionConfig connectionConfig : nodeConfig.getConnections()) {
                    ConnectionManager.createConnection(
                        nodeConfig.getNodeId(),  // Source node is the current node
                        connectionConfig.getTargetNodeId()
                    );
                }
            }
        }
    }
}