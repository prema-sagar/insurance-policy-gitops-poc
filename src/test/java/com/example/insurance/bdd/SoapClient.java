package com.example.insurance.bdd.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapClient {

    private final String baseUrl;
    private int statusCode;

    public SoapClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String callSoapService(String endpoint, String requestBody) {
        try {
            String fullUrl = baseUrl + endpoint;

            System.out.println("[SOAP URL] " + fullUrl);

            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();

            statusCode = conn.getResponseCode();

            BufferedReader br;
            if (statusCode >= 200 && statusCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            System.out.println("[SOAP RESPONSE] " + response);

            return response.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getStatusCode() {
        return statusCode;
    }
}