package com.nodeflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class YAMLConfigLoader {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static List<NodeConfig> loadConfig(String filePath) {
        try (InputStream inputStream = YAMLConfigLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + filePath);
            }

            // Load the YAML file as a Map and extract the "nodes" list as List<NodeConfig>
            Map<String, List<NodeConfig>> data = mapper.readValue(inputStream, new TypeReference<Map<String, List<NodeConfig>>>() {});
            return data.get("nodes");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load YAML configuration: " + e.getMessage());
        }
    }
}
