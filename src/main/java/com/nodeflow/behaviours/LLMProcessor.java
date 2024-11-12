package com.nodeflow.behaviours;

import java.util.Map;

import com.nodeflow.models.LLMFactory;
import com.nodeflow.models.LLMModel;
import com.nodeflow.nodes.Node;
import com.nodeflow.nodes.NodeRegistry;
import com.nodeflow.prompts.PromptRegistry;

public class LLMProcessor implements NodeBehaviour {
    
    private final LLMModel llmModel;
    private String instructions;
    private final String promptTemplate;

    public LLMProcessor(Map<String, Object> config) {
        String modelType = (String) config.getOrDefault("modelType", "openai");
        if (modelType == null) {
            throw new IllegalArgumentException("Missing required modelType for LLMProcessor configuration");
        }

        this.llmModel = LLMFactory.createModel(modelType, config);
        this.instructions = (String) config.get("instructions");
        this.promptTemplate = (String) config.get("promptTemplate");


    }

    @Override
    public void execute(Node node) {

        String prompt = buildPrompt(node);
        String response = llmModel.generateResponse(prompt);
        node.setOutput(response);
    }

    private String buildPrompt(Node node) {
        StringBuilder aggregatedData = new StringBuilder();
        
        for (String sourceId : node.getInputSources()) {
            Node sourceNode = NodeRegistry.findNodeById(sourceId);
            
            if (sourceNode != null) {
                String output = sourceNode.getOutput();
                if (output != null) {
                    aggregatedData.append(output).append("\n");
                } else {
                    System.out.println("Warning: Output from " + sourceId + " is null");
                }
            } else {
                System.out.println("Warning: Node with ID " + sourceId + " not found in registry");
            }
        }
        
        String template = PromptRegistry.getTemplate(promptTemplate);
        template = template.replace("{INSTRUCTIONS}", instructions != null ? instructions : "");
        template = template.replace("{DATA}", aggregatedData.toString());
        
        
        return template;
    }


    
}
