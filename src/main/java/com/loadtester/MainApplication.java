package com.loadtester;

public class MainApplication {
    public static void main(String[] args) {
        int targetRPS = 100; // default value, actual will be taken from command line
        int durationMinutes = 10; // default value, actual will be taken from command line
        String serverUrl = "https://c1i55mxsd6.execute-api.us-west-2.amazonaws.com/Live";
        String authKey = "sample_key";

        for (String arg : args) {
            String[] keyValue = arg.split("=");
            if (keyValue.length == 2) {
                if ("targetRPS".equalsIgnoreCase(keyValue[0])) {
                    targetRPS = Integer.parseInt(keyValue[1]);
                } else if ("durationMinutes".equalsIgnoreCase(keyValue[0])) {
                    durationMinutes = Integer.parseInt(keyValue[1]);
                } else if ("authKey".equalsIgnoreCase(keyValue[0])) {
                	authKey = keyValue[1];
                }
            }
        }

        LoadTester loadTester = new LoadTester(serverUrl, authKey, targetRPS, durationMinutes);
        try {
            loadTester.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
