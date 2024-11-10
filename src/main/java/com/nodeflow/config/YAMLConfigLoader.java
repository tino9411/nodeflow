package com.nodeflow.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YAMLConfigLoader {
  public static List<NodeConfig> loadConfig(String filePath) {
		// Createa a new instance of YAML parser
    Yaml yaml = new Yaml();

		// Initialise the list that will hold our final configuration objects
    List<NodeConfig> nodeConfigs = new ArrayList<>();

    try (InputStream inputStream =
        YAMLConfigLoader.class.getClassLoader().getResourceAsStream(filePath)) {

      // Load the YAML content as a Map
      Map<String, Object> data = yaml.load(inputStream);

      // Get the nodes list from the map and cast it properly
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> nodesData = (List<Map<String, Object>>) data.get("nodes");

      if (nodesData != null) {
        for (Map<String, Object> nodeData : nodesData) {
          NodeConfig nodeConfig = new NodeConfig();
          nodeConfig.setNodeId((String) nodeData.get("nodeId"));
          nodeConfig.setNodeType((String) nodeData.get("nodeType"));

          @SuppressWarnings("unchecked")
          List<Map<String, Object>> behavioursData =
              (List<Map<String, Object>>) nodeData.get("behaviours");

          List<BehaviourConfig> behaviourConfigs = new ArrayList<>();
          if (behavioursData != null) {
            for (Map<String, Object> behaviourData : behavioursData) {
              BehaviourConfig behaviourConfig = new BehaviourConfig();
              behaviourConfig.setType((String) behaviourData.get("type"));

              @SuppressWarnings("unchecked")
              Map<String, String> configMap = (Map<String, String>) behaviourData.get("config");

              behaviourConfig.setConfig(configMap);
              behaviourConfigs.add(behaviourConfig);
            }
          }

          nodeConfig.setBehaviours(behaviourConfigs);
          nodeConfigs.add(nodeConfig);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to load YAML configuration: " + e.getMessage());
    }

    return nodeConfigs;
  }
}
