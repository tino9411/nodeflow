package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.Map;
import java.util.stream.Collectors;

public class APIDataSource implements DataSource {
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
			String uriWithParams = buildUriWithParams(endpoint , parameters);
			URI uri = URI.create(uriWithParams);

			System.out.println("Making GET request to: " + uri);
            System.out.println("Headers: " + headers);
            System.out.println("Timeout: " + timeout + " seconds");

			HttpRequest request = buildHttpRequest(uri);
			
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			return handleResponse(response);

			
		} catch (HttpTimeoutException e) {
			System.err.println("Error: Request timed out after " + timeout + " seconds");
		} catch (InterruptedException | IOException e) {
			System.err.println("Error: Failed to fetch data from API - " + e.getMessage());
		}
		return null;
	}

	private String buildUriWithParams(String endpoint, Map<String, String> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return endpoint;
		}
		String paramString = parameters.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
		return endpoint + "?" + paramString;
	}

	private HttpRequest buildHttpRequest(URI uri) {
		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.timeout(java.time.Duration.ofSeconds(timeout));
				headers.forEach(requestBuilder::header);
		return requestBuilder.build();
	}

	private String handleResponse(HttpResponse<String> response) {
		if (response.statusCode() == 200) {
			return response.body();
		} else {
			System.err.println("Error: Received status code " + response.statusCode());
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
