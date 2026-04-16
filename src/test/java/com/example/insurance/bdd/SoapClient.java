package com.example.insurance.bdd;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SoapClient {

    private final String baseUrl;

    public SoapClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String call(String servicePath, String xmlRequest) {
        try {
            String fullUrl = baseUrl + "/" + servicePath;

            System.out.println("[SOAP URL] " + fullUrl);

            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(xmlRequest.getBytes());
            os.flush();

            Scanner scanner;

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                scanner = new Scanner(conn.getInputStream());
            } else {
                scanner = new Scanner(conn.getErrorStream());
            }

            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            System.out.println("[SOAP RESPONSE] " + response);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}