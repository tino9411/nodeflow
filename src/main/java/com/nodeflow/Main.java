package com.nodeflow;

import java.util.List;

import com.nodeflow.behaviours.DataFetcher;
import com.nodeflow.config.BehaviourConfig;
import com.nodeflow.config.NodeConfig;
import com.nodeflow.config.YAMLConfigLoader;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeType;

/** Hello world! */
public class Main {
  public static void main(String[] args) {
		List<NodeConfig> nodeConfigs = YAMLConfigLoader.loadConfig("config.yaml");

		for (NodeConfig nodeConfig : nodeConfigs) {
			Node node = new Node(NodeType.valueOf(nodeConfig.getNodeType()));
			for (BehaviourConfig behaviourConfig : nodeConfig.getBehaviours()) {
				if (behaviourConfig.getType().equals("DataFetcher")) {
					DataFetcher fetcher = new DataFetcher(behaviourConfig.getConfig());
					node.addBehaviour(fetcher);
					node.run();
				}
			}
		}

	}
} 
