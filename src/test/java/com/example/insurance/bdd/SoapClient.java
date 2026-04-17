package com.example.insurance.bdd;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SoapClient {

    private final String baseUrl;
    private int lastStatusCode = -1;

    public SoapClient(String baseUrl) {
        // Normalise: strip trailing slash so we can always append /serviceName
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    /**
     * POST a SOAP XML request to baseUrl/servicePath.
     * Reads both success (2xx) and fault (4xx/5xx) streams.
     */
    public String call(String servicePath, String xmlRequest) {
        try {
            String fullUrl = baseUrl + "/" + servicePath;
            System.out.println("[SOAP URL] " + fullUrl);

            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(xmlRequest.getBytes("UTF-8"));
            }

            lastStatusCode = conn.getResponseCode();

            Scanner scanner = (lastStatusCode >= 200 && lastStatusCode < 300)
                    ? new Scanner(conn.getInputStream(), "UTF-8")
                    : new Scanner(conn.getErrorStream(), "UTF-8");

            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            System.out.println("[SOAP RESPONSE " + lastStatusCode + "] " + response);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            lastStatusCode = -1;
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * GET baseUrl/path — used for the HealthCheckServlet mapped to /health.
     * HealthCheckServlet returns: "Application is running"  (HTTP 200)
     */
    public String callGet(String path) {
        try {
            String fullUrl = baseUrl + "/" + path;
            System.out.println("[GET URL] " + fullUrl);

            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(30000);

            lastStatusCode = conn.getResponseCode();

            Scanner scanner = (lastStatusCode >= 200 && lastStatusCode < 300)
                    ? new Scanner(conn.getInputStream(), "UTF-8")
                    : new Scanner(conn.getErrorStream(), "UTF-8");

            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            System.out.println("[GET RESPONSE " + lastStatusCode + "] " + response);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            lastStatusCode = -1;
            return "ERROR: " + e.getMessage();
        }
    }

    public int getLastStatusCode() {
        return lastStatusCode;
    }
}