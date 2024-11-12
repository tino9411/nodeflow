package com.nodeflow.models;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAIModel implements LLMModel {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIModel.class);
    private final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;
    private final String modelId;

    public OpenAIModel(Map<String, Object> config, String apiKey) {
        logger.debug("Initializing OpenAIModel with config: {}", config);
        
        if (apiKey == null || apiKey.isEmpty()) {
            logger.error("API key is null or empty");
            throw new IllegalArgumentException("API key is required for OpenAIModel");
        }
        this.apiKey = apiKey;
        
        // Set the model ID; assuming it's passed in the config for flexibility
        this.modelId = (String) config.getOrDefault("model", "gpt-4o-mini");  // Default to "gpt-4o"
        logger.info("OpenAIModel initialized with model ID: {}", modelId);
    }

    @Override
    public String generateResponse(String input) {
        logger.debug("Generating response for input: {}", input);
        
        try {
            // Build the JSON body for the API request
            JSONObject requestBody = createRequestBody(input);
            logger.trace("Created request body: {}", requestBody);

            // Construct the HTTP request
            HttpRequest request = createHttpRequest(requestBody);
            logger.debug("Sending request to OpenAI API");

            // Send the request and get the response
            HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            
            logger.debug("Received response with status code: {}", response.statusCode());

            // Check if the status code is 200 (OK)
            if (response.statusCode() != 200) {
                logger.error("OpenAI API request failed. Status code: {}, Response body: {}", 
                    response.statusCode(), response.body());
                throw new RuntimeException("OpenAI API request failed with status code: " + 
                    response.statusCode());
            }

            // Parse the response body
            return parseResponse(response.body());

        } catch (IOException e) {
            logger.error("IO Exception while calling OpenAI API", e);
            throw new RuntimeException("Failed to generate response from OpenAI API", e);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted while waiting for OpenAI API response", e);
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("Failed to generate response from OpenAI API", e);
        }
    }

    private JSONObject createRequestBody(String input) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", modelId);
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", input));
        requestBody.put("messages", messages);
        return requestBody;
    }

    private HttpRequest createHttpRequest(JSONObject requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
    }

    private String parseResponse(String responseBody) {
        logger.trace("Parsing response body: {}", responseBody);
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        if (!jsonResponse.has("choices")) {
            logger.error("No 'choices' field in OpenAI response: {}", jsonResponse);
            throw new RuntimeException("Unexpected response structure from OpenAI API");
        }
        
        JSONArray choices = jsonResponse.getJSONArray("choices");
        if (choices.length() == 0 || !choices.getJSONObject(0).has("message")) {
            logger.error("No valid message in 'choices': {}", jsonResponse);
            throw new RuntimeException("No valid message in OpenAI API response");
        }

        String content = choices.getJSONObject(0).getJSONObject("message").getString("content");
        logger.info("Successfully generated response from OpenAI");
        logger.debug("Generated content: {}", content);
        
        return content;
    }
}