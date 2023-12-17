package com.loadtester;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTester {
    private final int targetRps;
    private final int durationMinutes;
    private final HttpClientWrapper httpClientWrapper;
    private final AtomicInteger totalRequests = new AtomicInteger();
    private final AtomicInteger successfulResponses = new AtomicInteger();
    private final AtomicInteger otherResponses = new AtomicInteger();

    public LoadTester(String serverUrl, String authKey, int targetRps, int durationMinutes) {
        this.targetRps = targetRps;
        this.durationMinutes = durationMinutes;
        this.httpClientWrapper = new HttpClientWrapper(serverUrl, authKey, totalRequests, successfulResponses, otherResponses);
    }

    public void start() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(targetRps);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + TimeUnit.MINUTES.toMillis(durationMinutes);

        Runnable loadTask = () -> {
            while (System.currentTimeMillis() < endTime) {
                httpClientWrapper.sendRequest();
                try {
                    Thread.sleep(1000 / targetRps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < targetRps; i++) {
            executor.submit(loadTask);
        }

        executor.shutdown();
        executor.awaitTermination(durationMinutes, TimeUnit.MINUTES);

        System.out.println("Total Requests: " + totalRequests);
        System.out.println("Successful Responses: " + successfulResponses);
        System.out.println("Other Responses: " + otherResponses);
        System.out.println("Elapsed Time: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
    }
}
