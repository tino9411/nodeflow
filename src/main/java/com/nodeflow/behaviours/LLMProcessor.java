package com.nodeflow.behaviours;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nodeflow.models.LLMFactory;
import com.nodeflow.models.LLMModel;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeRegistry;

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
        StringBuilder aggregatedData = new StringBuilder();

        for (String sourceId : inputSource) {
            Node sourceNode = NodeRegistry.findNodeById(sourceId);
            
            if (sourceNode != null) {
                String output = sourceNode.getOutput();
                if (output != null) {
                    aggregatedData.append(output).append("\n");
                    System.out.println("Aggregated data from " + sourceId + ": " + output); // Debug log
                } else {
                    System.out.println("Warning: Output from " + sourceId + " is null");
                }
            } else {
                System.out.println("Warning: Node with ID " + sourceId + " not found in registry");
            }
        }

        // Pass aggregated data to the LLM model
        String response = llmModel.generateResponse(aggregatedData.toString());
        node.setOutput(response);
    }

    
}
