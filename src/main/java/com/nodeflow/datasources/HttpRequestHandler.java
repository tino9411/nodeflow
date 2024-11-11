package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.util.Map;

/**
 * HttpRequestHandler defines an interface for handling HTTP requests,
 * allowing flexibility in the underlying implementation of request sending.
 *
 * This interface is used in APIDataSource to decouple the data-fetching logic
 * from a specific HTTP client implementation.
 */

public interface HttpRequestHandler {
	/**
     * Sends a GET request to the specified URI with given headers and timeout.
     *
     * @param uri the target URI for the request
     * @param headers the headers to include in the request
     * @param timeout the request timeout in seconds
     * @return the response body as a String if the request is successful
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     * @throws HttpTimeoutException if the request times out
     */
    String sendRequest(URI uri, Map<String, String> headers, int timeout) throws IOException, InterruptedException, HttpTimeoutException;
    
}
