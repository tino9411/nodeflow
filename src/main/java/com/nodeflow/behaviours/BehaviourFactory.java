package com.nodeflow.behaviours;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BehaviourFactory {
    private static final Map<String, Function<Map<String, Object>, NodeBehaviour>> behaviourRegistry = new HashMap<>();

    static {
        // Register default behaviours
        behaviourRegistry.put("DataFetcher", DataFetcher::new);
        behaviourRegistry.put("LLMProcessor",LLMProcessor::new);
        // Additional behaviours can be registered here
    }

    // Method to register new behaviors
    public static void registerBehaviour(String type, Function<Map<String, Object>, NodeBehaviour> constructor) {
        behaviourRegistry.put(type, constructor);
    }

    // Method to create a behavior based on the type
    public static NodeBehaviour createBehaviour(String type, Map<String, Object> config) {
        Function<Map<String, Object>, NodeBehaviour> constructor = behaviourRegistry.get(type);

        if (constructor == null) {
            throw new IllegalArgumentException("Unknown behaviour type: " + type);
        }

        return constructor.apply(config);
    }
}