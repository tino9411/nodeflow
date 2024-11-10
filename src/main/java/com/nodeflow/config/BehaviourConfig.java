package com.nodeflow.config;

import java.util.Map;

public class BehaviourConfig {
	private String type;
	private Map<String, String> config;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}
}
