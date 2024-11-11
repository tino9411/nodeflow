package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class APIDataSourceTest {

    private String endpoint;
    private Map<String, String> headers;
    private Map<String, String> parameters;
    private int timeout;

    @Mock
    private HttpRequestHandler mockRequestHandler;

    private APIDataSource apiDataSource;

    @BeforeEach
    void setUp() {
        endpoint = "https://financialmodelingprep.com/api/v3/profile/AAPL";
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        parameters = new HashMap<>();
        parameters.put("apikey", "YQo7qP2o3Rd3CjjgQ84nPh31uJUdklQi");

        timeout = 5;

        // Instantiate APIDataSource with mock HttpRequestHandler
        apiDataSource = new APIDataSource(endpoint, headers, parameters, timeout, mockRequestHandler);
    }

    @Test
    void testFetchData_SuccessfulResponse() throws Exception {
        // Mock the HttpRequestHandler to simulate a successful response
        URI expectedUri = URI.create(endpoint + "?apikey=YQo7qP2o3Rd3CjjgQ84nPh31uJUdklQi");
        when(mockRequestHandler.sendRequest(expectedUri, headers, timeout))
                .thenReturn("{\"symbol\": \"AAPL\"}");

        String result = apiDataSource.fetchData();

        assertEquals("{\"symbol\": \"AAPL\"}", result);
    }

    @Test
    void testFetchData_Timeout() throws Exception {
        // Mock a timeout exception in HttpRequestHandler
        URI expectedUri = URI.create(endpoint + "?apikey=YQo7qP2o3Rd3CjjgQ84nPh31uJUdklQi");
        when(mockRequestHandler.sendRequest(expectedUri, headers, timeout))
                .thenThrow(new HttpTimeoutException("Request timed out"));

        // Expect HttpTimeoutException when fetchData is called
        HttpTimeoutException exception = assertThrows(HttpTimeoutException.class, () -> apiDataSource.fetchData());
        assertEquals("Request timed out", exception.getMessage());
    }

    @Test
    void testFetchData_IOError() throws Exception {
        // Mock an IOException in HttpRequestHandler
        URI expectedUri = URI.create(endpoint + "?apikey=YQo7qP2o3Rd3CjjgQ84nPh31uJUdklQi");
        when(mockRequestHandler.sendRequest(expectedUri, headers, timeout))
                .thenThrow(new IOException("I/O error"));

        // Expect IOException when fetchData is called
        IOException exception = assertThrows(IOException.class, () -> apiDataSource.fetchData());
        assertEquals("I/O error", exception.getMessage());
    }
}