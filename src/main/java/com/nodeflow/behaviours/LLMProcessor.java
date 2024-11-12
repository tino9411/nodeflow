package com.nodeflow.behaviours;

import java.util.Map;

import com.nodeflow.nodes.Node;

public class LLMProcessor implements NodeBehaviour {
    
    private final String modelType;

    public LLMProcessor(Map<String, Object> config) {
        this.modelType = (String) config.getOrDefault("modelType", "openai");
    }

    @Override
    public void execute(Node node) {
        String inputData = node.getInputData();
        System.out.println("LLMProcessor: Sending data to LLM - " + inputData);
        
        // Mock LLM processing
        String response = "Mocked LLM response for input: " + inputData;
        
        // Set the output of the node to the LLM's response
        node.setOutput(response);
        System.out.println("LLMProcessor: Received response from LLM - " + response);
    }

    
}
