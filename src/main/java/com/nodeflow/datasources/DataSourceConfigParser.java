package com.nodeflow.datasources;

import java.util.Collections;
import java.util.Map;

public class DataSourceConfigParser {

     // Helper method to extract headers as a Map<String, String>
    @SuppressWarnings("unchecked")
    public static Map<String, String> extractHeaders(Map<String, Object> config) {
        Object headersObj = config.get("headers");
        if (headersObj instanceof Map) {
            // Safe cast with extra check for Map<String, String>
            try {
                return (Map<String, String>) headersObj;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Headers should be a map of strings.", e);
            }
        }
        return Collections.emptyMap();
    }

    // Helper method to extract parameters as a Map<String, String>
    @SuppressWarnings("unchecked")
    public static Map<String, String> extractParameters(Map<String, Object> config) {
        Object paramsObj = config.get("params");
        if (paramsObj instanceof Map) {
            // Safe cast with extra check for Map<String, String>
            try {
                return (Map<String, String>) paramsObj;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Parameters should be a map of strings.", e);
            }
        }
        return Collections.emptyMap();
    }

    // Helper method to extract timeout, defaulting to 10 seconds if not specified
    public static int extractTimeout(Map<String, Object> config) {
        Object timeoutObj = config.get("timeout");
        if (timeoutObj instanceof Number) {
            return ((Number) timeoutObj).intValue();
        } else if (timeoutObj instanceof String) {
            try {
                return Integer.parseInt((String) timeoutObj);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Timeout should be an integer value.", e);
            }
        }
        // Default timeout if not specified
        return 10;
    }
}
