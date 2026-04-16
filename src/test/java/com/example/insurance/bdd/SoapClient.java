package com.example.insurance.bdd;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapClient {

    private final String baseUrl;

    public SoapClient(String baseUrl, String contextPath) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        this.baseUrl = baseUrl + "/" + contextPath;
    }

    // ---------------- CLAIMS ----------------

    public String createClaim(String policy, Double amount) throws Exception {
        String body = envelope(
                "<ins:createClaim>" +
                        "<policyNumber>" + policy + "</policyNumber>" +
                        "<amount>" + amount + "</amount>" +
                        "</ins:createClaim>"
        );
        return call("/ClaimsSoapService", body);
    }

    public String getClaimById(String id) throws Exception {
        String body = envelope(
                "<ins:getClaimById>" +
                        "<claimId>" + id + "</claimId>" +   // ✅ FIXED
                        "</ins:getClaimById>"
        );
        return call("/ClaimsSoapService", body);
    }

    public String getAllClaims() throws Exception {
        String body = envelope("<ins:getAllClaims/>");
        return call("/ClaimsSoapService", body);
    }

    public String updateClaimStatus(String id, String status) throws Exception {
        String body = envelope(
                "<ins:updateClaimStatus>" +
                        "<claimId>" + id + "</claimId>" +   // ✅ FIXED
                        "<status>" + status + "</status>" +
                        "</ins:updateClaimStatus>"
        );
        return call("/ClaimsSoapService", body);
    }

    public String deleteClaim(String id) throws Exception {
        String body = envelope(
                "<ins:deleteClaim>" +
                        "<claimId>" + id + "</claimId>" +   // ✅ FIXED
                        "</ins:deleteClaim>"
        );
        return call("/ClaimsSoapService", body);
    }

    // ---------------- POLICY ----------------

    public String getPolicyDetails(String policyNumber) throws Exception {
        String body = envelope(
                "<ins:getPolicyDetails>" +
                        "<policyNumber>" + policyNumber + "</policyNumber>" +
                        "</ins:getPolicyDetails>"
        );
        return call("/HealthPolicySoapService", body);
    }

    public String getPolicyStatus(String policyNumber) throws Exception {
        String body = envelope(
                "<ins:getPolicyStatus>" +
                        "<policyNumber>" + policyNumber + "</policyNumber>" +
                        "</ins:getPolicyStatus>"
        );
        return call("/HealthPolicySoapService", body);
    }

    // ---------------- HEALTH ----------------

    public HttpResponse healthCheck() throws Exception {
        URL url = new URL(baseUrl + "/healthCheck"); // adjust if needed
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        String body = read(status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream());

        return new HttpResponse(status, body);
    }

    // ---------------- CORE ----------------

    private String envelope(String innerBody) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                innerBody +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

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
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        String response = read(is);

        System.out.println("[SOAP RESPONSE] " + response);

        return response;
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