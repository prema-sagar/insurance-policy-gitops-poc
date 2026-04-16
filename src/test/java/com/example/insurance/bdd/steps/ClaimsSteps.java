package com.example.insurance.bdd;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapClient {

    private final String baseUrl;

    // ✅ Updated constructor
    public SoapClient(String baseUrl, String contextPath) {
        // remove trailing slash if any
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        this.baseUrl = baseUrl + "/" + contextPath;
    }

    // ---------------- CLAIMS ----------------

    public String createClaim(String policy, Double amount) throws Exception {
        String soapBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<createClaim>" +
                "<policyNumber>" + policy + "</policyNumber>" +
                "<amount>" + amount + "</amount>" +
                "</createClaim>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoapService("/ClaimsSoapService", soapBody);
    }

    public String getClaimById(String id) throws Exception {
        String soapBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<getClaimById>" +
                "<id>" + id + "</id>" +
                "</getClaimById>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoapService("/ClaimsSoapService", soapBody);
    }

    public String getAllClaims() throws Exception {
        String soapBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<getAllClaims/>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoapService("/ClaimsSoapService", soapBody);
    }

    public String updateClaimStatus(String id, String status) throws Exception {
        String soapBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<updateClaimStatus>" +
                "<id>" + id + "</id>" +
                "<status>" + status + "</status>" +
                "</updateClaimStatus>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoapService("/ClaimsSoapService", soapBody);
    }

    public String deleteClaim(String id) throws Exception {
        String soapBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<deleteClaim>" +
                "<id>" + id + "</id>" +
                "</deleteClaim>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoapService("/ClaimsSoapService", soapBody);
    }

    // ---------------- POLICY ----------------

    public String getPolicyDetails(String policyNumber) throws Exception {
        String soapBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<getPolicyDetails>" +
                "<policyNumber>" + policyNumber + "</policyNumber>" +
                "</getPolicyDetails>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoapService("/HealthPolicySoapService", soapBody);
    }

    public String getPolicyStatus(String policyNumber) throws Exception {
        String soapBody =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<getPolicyStatus>" +
                "<policyNumber>" + policyNumber + "</policyNumber>" +
                "</getPolicyStatus>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoapService("/HealthPolicySoapService", soapBody);
    }

    // ---------------- HEALTH ----------------

    public HttpResponse healthCheck() throws Exception {
        URL url = new URL(baseUrl + "/health");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        String body = readStream(
                status >= 200 && status < 300
                        ? conn.getInputStream()
                        : conn.getErrorStream()
        );

        return new HttpResponse(status, body);
    }

    // ---------------- CORE METHOD ----------------

    private String callSoapService(String path, String body) throws Exception {
        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        int status = conn.getResponseCode();

        InputStream stream = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        return readStream(stream);
    }

    private String readStream(InputStream is) throws Exception {
        if (is == null) return null;

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    // ---------------- RESPONSE CLASS ----------------

    public static class HttpResponse {
        public int statusCode;
        public String body;

        public HttpResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
    }
}