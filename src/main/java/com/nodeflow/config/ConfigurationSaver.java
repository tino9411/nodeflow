package com.nodeflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigurationSaver {

    // Configure ObjectMapper with YAMLFactory to handle YAML format
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    /**
     * Saves a list of NodeConfig objects to a YAML file.
     * @param nodeConfigs List of NodeConfig objects to save.
     * @param filename The path to the YAML file.
     * @throws IOException If an I/O error occurs during saving.
     */
    public static void saveConfig(List<NodeConfig> nodeConfigs, String filename) throws IOException {
        // Wrap nodeConfigs in a map with a key "nodes" to match the structure of your existing YAML
        mapper.writeValue(new File(filename), Map.of("nodes", nodeConfigs));
    }

    /**
     * Loads a list of NodeConfig objects from a YAML file.
     * @param filename The path to the YAML file.
     * @return List of NodeConfig objects loaded from the YAML file.
     * @throws IOException If an I/O error occurs during loading.
     */
    public static List<NodeConfig> loadConfig(String filename) throws IOException {
        // Read the YAML file and extract the "nodes" section as a list of NodeConfig
        Map<String, List<NodeConfig>> data = mapper.readValue(new File(filename), Map.class);
        return data.get("nodes");
    }
}
