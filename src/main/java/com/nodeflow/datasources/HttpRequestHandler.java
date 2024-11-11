package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.util.Map;

public interface HttpRequestHandler {
    String sendRequest(URI uri, Map<String, String> headers, int timeout) throws IOException, InterruptedException, HttpTimeoutException;
    
}
