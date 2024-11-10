package com.nodeflow.datasources;

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

                Map<String, String> headers = DataSourceConfigParser.extractHeaders(config);
                Map<String, String> parameters = DataSourceConfigParser.extractParameters(config);
                int timeout = DataSourceConfigParser.extractTimeout(config);

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

   
}