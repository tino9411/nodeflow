package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class APIDataSource implements DataSource {
	private static final Logger logger = Logger.getLogger(APIDataSource.class.getName());
	
	private final String endpoint;
	private final Map<String, String> headers;
	private final Map<String, String> parameters;
	private final int timeout;

	public APIDataSource(String endpoint, Map<String, String> headers, Map<String, String> parameters, int timeout) {
		this.endpoint = endpoint;
		this.headers = headers;
		this.parameters = parameters;
		this.timeout = timeout > 0 ? timeout : 10;
	}

	@Override
	public String fetchData() {
		try {
			URI uri = buildUriWithParams();
			HttpRequest request = buildHttpRequest(uri);
			
			logger.log(Level.INFO, "Making GET request to: {0}", uri);
			logger.log(Level.INFO, "Headers: {0}", headers);
			logger.log(Level.INFO, "Timeout: {0} seconds", timeout);

			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			return handleResponse(response);

		} catch (HttpTimeoutException e) {
			logger.log(Level.WARNING, "Request timed out after " + timeout + " seconds", e);
		} catch (InterruptedException | IOException e) {
			logger.log(Level.SEVERE, "Failed to fetch data from API", e);
		}
		return null;
	}

	private URI buildUriWithParams() {
		if (parameters == null || parameters.isEmpty()) {
			return URI.create(endpoint);
		}
		String paramString = parameters.entrySet().stream()
				.map(entry -> entry.getKey() + "=" + entry.getValue())
				.collect(Collectors.joining("&"));
		return URI.create(endpoint + "?" + paramString);
	}

	private HttpRequest buildHttpRequest(URI uri) {
		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.timeout(Duration.ofSeconds(timeout));
		headers.forEach(requestBuilder::header);
		return requestBuilder.build();
	}

	private String handleResponse(HttpResponse<String> response) {
		if (response.statusCode() == 200) {
			logger.info("Received successful response from API");
			return response.body();
		} else {
			logger.warning("Received error response with status code {0}");
			return null;
		}
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