package com.nodeflow.datasources;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.Map;

public class HttpClientRequestHandler implements HttpRequestHandler {

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public String sendRequest(URI uri, Map<String, String> headers, int timeout) throws IOException, InterruptedException, HttpTimeoutException{

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).GET().timeout(java.time.Duration.ofSeconds(timeout));
        headers.forEach(requestBuilder::header);
        HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
         if (response.statusCode() == 200) {
            return response.body();
         } else {
            throw new IOException("Received non-200 response: " + response.statusCode());
         }
    }
    
}
