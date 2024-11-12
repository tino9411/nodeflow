package com.nodeflow.models;

/**
 * LLMModel is an interface for various language models.
 * It provides a standard method for generating a response based on input data.
 */
public interface LLMModel {
    /**
     * Generates a response based on the input data.
     *
     * @param input The input text to send to the LLM.
     * @return The generated response from the LLM.
     */
    String generateResponse(String input);
    
}
