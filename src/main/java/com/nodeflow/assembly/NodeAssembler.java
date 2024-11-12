package com.nodeflow.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nodeflow.config.BehaviourConfig;
import com.nodeflow.config.NodeConfig;
import com.nodeflow.config.YAMLConfigLoader;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeBuilder;
import com.nodeflow.nodes.NodeType;

/**
 * NodeAssembler is responsible for orchestrating the creation of Node instances.
 * It acts as a high-level manager that assembles nodes based on different configuration sources.
 * This class enables flexibility in creating nodes from both configuration files and custom input, 
 * without needing to modify core node creation logic.
 */
public class NodeAssembler {

    /**
     * Creates a list of Node instances based on configurations loaded from a YAML file.
     * This method reads the configuration file, initializes each node with the specified 
     * properties and behaviors, and returns a list of fully assembled nodes.
     *
     * @param configPath The path to the YAML configuration file containing node definitions.
     * @return A list of Node instances created based on the YAML configurations.
     */
    public List<Node> createNodesFromConfig(String configPath) {
        List<NodeConfig> nodeConfigs = YAMLConfigLoader.loadConfig(configPath);
        List<Node> nodes = new ArrayList<>();

        for (NodeConfig nodeConfig : nodeConfigs) {
            NodeBuilder nodeBuilder = new NodeBuilder()
                    .setNodeId(nodeConfig.getNodeId())
                    .setNodeType(NodeType.valueOf(nodeConfig.getNodeType()));

            for (BehaviourConfig behaviour : nodeConfig.getBehaviours()) {
                nodeBuilder.addBehaviour(behaviour.getType(), behaviour.getConfig());
            }

            nodes.add(nodeBuilder.build());
        }

        return nodes;
    }

    /**
     * Creates a single Node instance programmatically, allowing for dynamic creation 
     * based on custom input. This method sets up the node with a specified ID, type, 
     * and a single behavior configuration.
     *
     * @param nodeId The unique identifier for the node.
     * @param nodeType The type of the node, as specified by the NodeType enum.
     * @param behaviourType The type of the behavior to be added to the node.
     * @param config A map containing the configuration details for the behavior.
     * @return A single, fully assembled Node instance based on the specified parameters.
     */
    public Node createNode(String nodeId, NodeType nodeType, String behaviourType, Map<String, Object> config) {
        return new NodeBuilder()
                .setNodeId(nodeId)
                .setNodeType(nodeType)
                .addBehaviour(behaviourType, config)
                .build();
    }
}