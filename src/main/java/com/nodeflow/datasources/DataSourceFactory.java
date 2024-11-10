package com.nodeflow.datasources;

import java.util.Collections;
import java.util.Map;

public class DataSourceFactory {

    public static DataSource createDataSource(Map<String, Object> config) {
        // Get the source type and validate it
        String sourceType = (String) config.get("sourceType");
        if (sourceType == null) {
            throw new IllegalArgumentException("sourceType must be specified in the configuration.");
        }

        switch (sourceType) {
            case "API":
                // Retrieve the endpoint, headers, parameters, and timeout from the configuration
                String endpoint = (String) config.get("endpoint");
                if (endpoint == null) {
                    throw new IllegalArgumentException("endpoint must be specified for API data source.");
                }

                Map<String, String> headers = extractHeaders(config);
                Map<String, String> parameters = extractParameters(config);
                int timeout = extractTimeout(config);

                // Create and return the ApiDataSource
                return new APIDataSource(endpoint, headers, parameters, timeout);

            case "Database":
                // Retrieve the connection string for database data source
                String connectionString = (String) config.get("connectionString");
                if (connectionString == null) {
                    throw new IllegalArgumentException("connectionString must be specified for Database data source.");
                }

                // Create and return the DatabaseDataSource
                return new DatabaseDataSource(connectionString);

            default:
                throw new IllegalArgumentException("Unknown data source type: " + sourceType);
        }
    }

    // Helper method to extract headers as a Map<String, String>
    @SuppressWarnings("unchecked")
    private static Map<String, String> extractHeaders(Map<String, Object> config) {
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
    private static Map<String, String> extractParameters(Map<String, Object> config) {
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
    private static int extractTimeout(Map<String, Object> config) {
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