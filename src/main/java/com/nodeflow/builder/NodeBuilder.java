package com.nodeflow.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nodeflow.behaviours.DataFetcher;
import com.nodeflow.config.BehaviourConfig;
import com.nodeflow.config.NodeConfig;
import com.nodeflow.config.NodeConfigFactory;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeType;;

public class NodeBuilder {

    private String nodeId;
    private NodeType nodeType;
    private  final List<BehaviourConfig> behaviours = new ArrayList<>();

    public NodeBuilder setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public NodeBuilder setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
        return this;
    }

    public NodeBuilder addBehaviour(String behaviourType, Map<String, Object> config) {
        BehaviourConfig behaviourConfig = new BehaviourConfig();
        behaviourConfig.setType(behaviourType);
        behaviourConfig.setConfig(config);
        behaviours.add(behaviourConfig);
        return this;
    }

    public Node build() {
        NodeConfig nodeConfig = NodeConfigFactory.createNodeConfig(nodeId, nodeType.name(), behaviours);
        Node node = new Node(NodeType.valueOf(nodeConfig.getNodeType()));
        
        for (BehaviourConfig behaviourConfig : nodeConfig.getBehaviours()) {
            if ("DataFetcher".equals(behaviourConfig.getType())) {
                node.addBehaviour(new DataFetcher(behaviourConfig.getConfig()));
            }
            // Additional behaviour types can be added here
        }
        
        return node;
    }
}