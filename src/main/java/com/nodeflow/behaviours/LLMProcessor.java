package com.nodeflow.behaviours;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nodeflow.models.LLMFactory;
import com.nodeflow.models.LLMModel;
import com.nodeflow.nodes.Node;

public class LLMProcessor implements NodeBehaviour {
    
    private final LLMModel llmModel;
    private List<String> inputSource;

    public LLMProcessor(Map<String, Object> config) {
        String modelType = (String) config.getOrDefault("modelType", "openai");
        if (modelType == null) {
            throw new IllegalArgumentException("Missing required modelType for LLMProcessor configuration");
        }

        this.llmModel = LLMFactory.createModel(modelType, config);

        Object inputSourceObj = config.get("inputSource");
        if (inputSourceObj instanceof List) {
            this.inputSource = new ArrayList<>((List<String>) inputSourceObj);
        } else {
            this.inputSource = new ArrayList<>();
            System.err.println("Warning: 'inputSource' is either missing or not a List. Defaulting to empty list.");
        }


    }

    @Override
    public void execute(Node node) {
        String inputData = node.getInputData();
        // Mock LLM processingocked LLM respons
        String response = llmModel.generateResponse(inputData);
        
        // Set the output of the node to the LLM's response
        node.setOutput(response);
    }

    
}
