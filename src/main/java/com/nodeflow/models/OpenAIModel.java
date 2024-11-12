package com.nodeflow.models;
import java.util.Map;

public class OpenAIModel implements LLMModel {

    private final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;
    

    public OpenAIModel(Map<String, Object> config, String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is required for OpenAIModel");
        }
        this.apiKey = apiKey;
        
       

    }

    @Override
    public String generateResponse(String input) {
     // Mock API call; in reality, you'd make an HTTP request to OpenAI here, using apiUrl and apiKey
     System.out.println("OpenAIModel: Sending input to OpenAI API - " + input);
     return "Mocked response from OpenAI for input: " + input;
 }
    
}
