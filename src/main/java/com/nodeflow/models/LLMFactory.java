package com.nodeflow.models;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LLMFactory {

    private static final Map<String, Function<Map<String, Object>, LLMModel>> modelRegistry = new HashMap<>();

    static {
        modelRegistry.put("openai", config -> new OpenAIModel(config, System.getenv("OPENAI_API_KEY")));

    }

    public static LLMModel createModel(String modelType, Map<String, Object> config) {
        Function<Map<String, Object>, LLMModel> constructor = modelRegistry.get(modelType);

        if (constructor == null) {
            throw new IllegalArgumentException("Unknown LLM model type: " + modelType);
        }

        return constructor.apply(config);
    }


    
}
