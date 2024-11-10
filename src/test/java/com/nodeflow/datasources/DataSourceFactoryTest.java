package com.nodeflow.datasources;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class DataSourceFactoryTest {

    @Test
void testCreateAPIDataSourceWithHeadersAndParams() {
    Map<String, Object> config = new HashMap<>();
    config.put("sourceType", "API");
    config.put("endpoint", "https://jsonplaceholder.typicode.com/posts");
    
    // Add headers
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer sample_token");
    headers.put("Content-Type", "application/json");
    config.put("headers", headers);

    // Add parameters
    Map<String, String> params = new HashMap<>();
    params.put("userId", "1");
    config.put("params", params);

    // Add timeout
    config.put("timeout", 5);

    // Create DataSource
    DataSource dataSource = DataSourceFactory.createDataSource(config);

    assertTrue(dataSource instanceof APIDataSource, "Expected an instance of APIDataSource");

    // Test casting and properties
    APIDataSource apiDataSource = (APIDataSource) dataSource;
    assertEquals("https://jsonplaceholder.typicode.com/posts", apiDataSource.getEndpoint());
    assertEquals(headers, apiDataSource.getHeaders());
    assertEquals(params, apiDataSource.getParameters());
    assertEquals(5, apiDataSource.getTimeout());

    // Call fetchData to verify API request and print statements
    String response = apiDataSource.fetchData();
    assertNotNull(response, "Response should not be null");
    assertTrue(response.contains("userId"), "Response should contain 'userId'");
}

@Test
void testCreateAPIDataSourceWithFMPConfig() {
    Map<String, Object> config = new HashMap<>();
    config.put("sourceType", "API");
    config.put("endpoint", "https://financialmodelingprep.com/api/v3/profile/AAPL");
    
    // Add headers
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    config.put("headers", headers);

    // Add parameters (including the API key for authentication)
    Map<String, String> params = new HashMap<>();
    params.put("apikey", "YQo7qP2o3Rd3CjjgQ84nPh31uJUdklQi");  // Replace with your actual API key
    config.put("params", params);

    // Add timeout
    config.put("timeout", 10);

    // Create DataSource
    DataSource dataSource = DataSourceFactory.createDataSource(config);

    assertTrue(dataSource instanceof APIDataSource, "Expected an instance of ApiDataSource");

    // Test casting and properties
    APIDataSource apiDataSource = (APIDataSource) dataSource;
    assertEquals("https://financialmodelingprep.com/api/v3/profile/AAPL", apiDataSource.getEndpoint());
    assertEquals(headers, apiDataSource.getHeaders());
    assertEquals(params, apiDataSource.getParameters());
    assertEquals(10, apiDataSource.getTimeout());

    // Call fetchData to verify API request and print statements
    String response = apiDataSource.fetchData();
    assertNotNull(response, "Response should not be null");
    assertTrue(response.contains("AAPL"), "Response should contain 'AAPL'");
}
    @Test
    void testCreateDatabaseDataSource() {
        Map<String, Object> config = new HashMap<>();
        config.put("sourceType", "Database");
        config.put("connectionString", "jdbc:mysql://localhost:3306/mydb");

        // Create DataSource
        DataSource dataSource = DataSourceFactory.createDataSource(config);

        assertTrue(dataSource instanceof DatabaseDataSource, "Expected an instance of DatabaseDataSource");

        // Test casting and properties
        DatabaseDataSource dbDataSource = (DatabaseDataSource) dataSource;
        assertEquals("jdbc:mysql://localhost:3306/mydb", dbDataSource.getConnectionString());
    }

    @Test
    void testCreateAPIDataSourceWithInvalidTimeout() {
        Map<String, Object> config = new HashMap<>();
        config.put("sourceType", "API");
        config.put("endpoint", "https://jsonplaceholder.typicode.com/posts");
        config.put("timeout", "invalid");

        // Expect IllegalArgumentException due to invalid timeout
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DataSourceFactory.createDataSource(config);
        });

        assertTrue(exception.getMessage().contains("Timeout should be an integer value"));
    }
}