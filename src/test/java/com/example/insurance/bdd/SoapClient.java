package com.example.insurance.bdd;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapClient {

    private final String baseUrl;

    // ✅ Constructor with context path
    public SoapClient(String baseUrl, String contextPath) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        this.baseUrl = baseUrl + "/" + contextPath;
    }

    // ---------------- CLAIMS ----------------

    public String createClaim(String policy, Double amount) throws Exception {
        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<createClaim>" +
                "<policyNumber>" + policy + "</policyNumber>" +
                "<amount>" + amount + "</amount>" +
                "</createClaim>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return call("/ClaimsSoapService", body);
    }

    public String getClaimById(String id) throws Exception {
        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<getClaimById><id>" + id + "</id></getClaimById>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return call("/ClaimsSoapService", body);
    }

    public String getAllClaims() throws Exception {
        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body><getAllClaims/></soapenv:Body></soapenv:Envelope>";

        return call("/ClaimsSoapService", body);
    }

    public String updateClaimStatus(String id, String status) throws Exception {
        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<updateClaimStatus><id>" + id + "</id><status>" + status + "</status></updateClaimStatus>" +
                "</soapenv:Body></soapenv:Envelope>";

        return call("/ClaimsSoapService", body);
    }

    public String deleteClaim(String id) throws Exception {
        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body><deleteClaim><id>" + id + "</id></deleteClaim></soapenv:Body></soapenv:Envelope>";

        return call("/ClaimsSoapService", body);
    }

    // ---------------- POLICY ----------------

    public String getPolicyDetails(String policyNumber) throws Exception {
        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body><getPolicyDetails><policyNumber>" + policyNumber + "</policyNumber></getPolicyDetails></soapenv:Body></soapenv:Envelope>";

        return call("/HealthPolicySoapService", body);
    }

    public String getPolicyStatus(String policyNumber) throws Exception {
        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body><getPolicyStatus><policyNumber>" + policyNumber + "</policyNumber></getPolicyStatus></soapenv:Body></soapenv:Envelope>";

        return call("/HealthPolicySoapService", body);
    }

    // ---------------- HEALTH ----------------

    public HttpResponse healthCheck() throws Exception {
        URL url = new URL(baseUrl + "/health");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        int status = conn.getResponseCode();
        String body = read(status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream());

        return new HttpResponse(status, body);
    }

    // ---------------- CORE ----------------

    private String call(String path, String body) throws Exception {
        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

        return read(is);
    }

    private String read(InputStream is) throws Exception {
        if (is == null) return null;

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    public static class HttpResponse {
        public int statusCode;
        public String body;

        public HttpResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
    }
}