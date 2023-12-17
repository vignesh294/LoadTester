package com.loadtester;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpClientWrapper {
    private final HttpClient client;
    private final String serverUrl;
    private final String authKey;
    private final AtomicInteger totalRequests;
    private final AtomicInteger successfulResponses;
    private final AtomicInteger otherResponses;

    public HttpClientWrapper(String serverUrl, String authKey, AtomicInteger totalRequests, AtomicInteger successfulResponses, AtomicInteger otherResponses) {
        this.client = HttpClient.newHttpClient();
        this.serverUrl = serverUrl;
        this.authKey = authKey;
        this.totalRequests = totalRequests;
        this.successfulResponses = successfulResponses;
        this.otherResponses = otherResponses;
    }

    public void sendRequest() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("X-Api-Key", authKey)
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"YOURNAME\", \"date\": \"" + Instant.now().toString() + "\", \"requests_sent\": " + totalRequests.get() + "}"))
                .build();

        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        response.thenAccept(res -> {
            totalRequests.incrementAndGet();
            if (res.statusCode() == 200) {
                successfulResponses.incrementAndGet();
            } else {
                otherResponses.incrementAndGet();
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }
}

