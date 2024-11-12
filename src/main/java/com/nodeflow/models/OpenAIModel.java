package com.nodeflow.models;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIModel implements LLMModel {

    private final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;
    private final String modelId;

    public OpenAIModel(Map<String, Object> config, String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is required for OpenAIModel");
        }
        this.apiKey = apiKey;
        
        // Set the model ID; assuming it's passed in the config for flexibility
        this.modelId = (String) config.getOrDefault("model", "gpt-4o-mini");  // Default to "gpt-4o"
    }

    @Override
public String generateResponse(String input) {
    try {
        // Build the JSON body for the API request
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", modelId);

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", input));
        requestBody.put("messages", messages);

        // Construct the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Check if the status code is 200 (OK)
        if (response.statusCode() != 200) {
            System.err.println("OpenAI API request failed with status code: " + response.statusCode());
            System.err.println("Response body: " + response.body());
            throw new RuntimeException("OpenAI API request failed with status code: " + response.statusCode());
        }

        // Parse the response body
        JSONObject responseBody = new JSONObject(response.body());
        
        if (!responseBody.has("choices")) {
            System.err.println("No 'choices' field in OpenAI response: " + responseBody);
            throw new RuntimeException("Unexpected response structure from OpenAI API");
        }
        
        // Retrieve and return the content
        JSONArray choices = responseBody.getJSONArray("choices");
        if (choices.length() == 0 || !choices.getJSONObject(0).has("message")) {
            System.err.println("No valid message in 'choices': " + responseBody);
            throw new RuntimeException("No valid message in OpenAI API response");
        }

        String content = choices.getJSONObject(0).getJSONObject("message").getString("content");
        System.out.println("Received response from OpenAI: " + content);
        return content;

    } catch (IOException | InterruptedException e) {
        throw new RuntimeException("Failed to generate response from OpenAI API", e);
    }
}
}