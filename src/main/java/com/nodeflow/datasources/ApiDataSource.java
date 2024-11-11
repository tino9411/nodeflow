package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * APIDataSource is responsible for handling HTTP GET requests to a specified endpoint,
 * utilizing customizable headers, parameters, and a configurable timeout.
 *
 * This class depends on the HttpRequestHandler interface to send requests, allowing
 * flexibility in the underlying HTTP client implementation.
 */

public class APIDataSource implements DataSource {
	private static final Logger logger = Logger.getLogger(APIDataSource.class.getName());
	
	private final String endpoint;
	private final Map<String, String> headers;
	private final Map<String, String> parameters;
	private final int timeout;
	private final HttpRequestHandler httpRequestHandler;

	/**
     * Constructs an APIDataSource with the specified endpoint, headers, parameters,
     * timeout, and HttpRequestHandler instance.
     *
     * @param endpoint the base URL of the API endpoint
     * @param headers HTTP headers to include in the request
     * @param parameters URL parameters to include in the request
     * @param timeout timeout for the request, in seconds
     * @param httpRequestHandler instance of HttpRequestHandler to handle HTTP requests
     */

	public APIDataSource(String endpoint, Map<String, String> headers, Map<String, String> parameters, int timeout, HttpRequestHandler httpRequestHandler) {
		this.endpoint = endpoint;
		this.headers = headers;
		this.parameters = parameters;
		this.timeout = timeout > 0 ? timeout : 10;
		this.httpRequestHandler = httpRequestHandler;
	}

	/**
     * Fetches data from the configured API endpoint using a GET request.
     *
     * @return the response body as a String if the request is successful; null if an error occurs
     * @throws IOException if an I/O error occurs during the request
     * @throws InterruptedException if the request is interrupted
     * @throws HttpTimeoutException if the request times out
     */

	@Override
	public String fetchData() throws IOException, InterruptedException, HttpTimeoutException {
			URI uri = buildUriWithParams();
			logger.log(Level.INFO, "Making GET request to: {0}", uri);
			logger.log(Level.INFO, "Headers: {0}", headers);
			logger.log(Level.INFO, "Timeout: {0} seconds", timeout);

			return httpRequestHandler.sendRequest(uri, headers, timeout);		
	}
	
	/**
     * Builds the URI with the provided parameters appended as query parameters.
     *
     * @return the URI with query parameters appended
     */
	private URI buildUriWithParams() {
		if (parameters == null || parameters.isEmpty()) {
			return URI.create(endpoint);
		}
		String paramString = parameters.entrySet().stream()
				.map(entry -> entry.getKey() + "=" + entry.getValue())
				.collect(Collectors.joining("&"));
		return URI.create(endpoint + "?" + paramString);
	}

	public String getEndpoint() {
		return endpoint;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public int getTimeout() {
		return timeout;
	}
}
