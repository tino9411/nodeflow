package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpTimeoutException;
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
	private final HttpRequestHandler httpRequestHandler;

	public APIDataSource(String endpoint, Map<String, String> headers, Map<String, String> parameters, int timeout, HttpRequestHandler httpRequestHandler) {
		this.endpoint = endpoint;
		this.headers = headers;
		this.parameters = parameters;
		this.timeout = timeout > 0 ? timeout : 10;
		this.httpRequestHandler = httpRequestHandler;
	}

	@Override
	public String fetchData() throws IOException, InterruptedException, HttpTimeoutException {
			URI uri = buildUriWithParams();
			logger.log(Level.INFO, "Making GET request to: {0}", uri);
			logger.log(Level.INFO, "Headers: {0}", headers);
			logger.log(Level.INFO, "Timeout: {0} seconds", timeout);

			return httpRequestHandler.sendRequest(uri, headers, timeout);		
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