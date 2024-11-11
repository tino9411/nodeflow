package com.nodeflow.config;

import java.util.ArrayList;
import java.util.List;


public class NodeConfigFactory {

	public static NodeConfig createNodeConfig(String nodeId, String nodeType, List<BehaviourConfig> behaviours) {
		NodeConfig nodeConfig = new NodeConfig();
		nodeConfig.setNodeId(nodeId);
		nodeConfig.setNodeType(nodeType);
		nodeConfig.setBehaviours(behaviours != null ? behaviours : new ArrayList<>());
		return nodeConfig;
	}

	public static NodeConfig createDefaultNodeConfig(String nodeId, String nodeType) {
		NodeConfig nodeConfig = new NodeConfig();
		nodeConfig.setNodeId(nodeId);
		nodeConfig.setNodeType(nodeType);
		nodeConfig.setBehaviours(getDefaultBehaviours(nodeType));
		return nodeConfig;
	}

	private static List<BehaviourConfig> getDefaultBehaviours(String nodeType) {
		List<BehaviourConfig> defaultBhaviours = new ArrayList<>();

		return defaultBhaviours;
	}
}
