package com.example.insurance.bdd;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapClient {

    private final String baseUrl;

    public SoapClient(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        this.baseUrl = baseUrl;
    }

    // ---------------- CLAIMS ----------------

    public String createClaim(String policy, Double amount) throws Exception {
        return call("/ClaimsSoapService", envelope(
                "<ins:createClaim>" +
                        "<policyNumber>" + policy + "</policyNumber>" +
                        "<amount>" + amount + "</amount>" +
                        "</ins:createClaim>"
        ));
    }

    public String getClaimById(String id) throws Exception {
        return call("/ClaimsSoapService", envelope(
                "<ins:getClaimById>" +
                        "<claimId>" + id + "</claimId>" +
                        "</ins:getClaimById>"
        ));
    }

    public String getAllClaims() throws Exception {
        return call("/ClaimsSoapService", envelope("<ins:getAllClaims/>"));
    }

    public String updateClaimStatus(String id, String status) throws Exception {
        return call("/ClaimsSoapService", envelope(
                "<ins:updateClaimStatus>" +
                        "<claimId>" + id + "</claimId>" +
                        "<status>" + status + "</status>" +
                        "</ins:updateClaimStatus>"
        ));
    }

    public String deleteClaim(String id) throws Exception {
        return call("/ClaimsSoapService", envelope(
                "<ins:deleteClaim>" +
                        "<claimId>" + id + "</claimId>" +
                        "</ins:deleteClaim>"
        ));
    }

    // ---------------- POLICY ----------------

    public String getPolicyDetails(String policyNumber) throws Exception {
        return call("/HealthPolicySoapService", envelope(
                "<ins:getPolicyDetails>" +
                        "<policyNumber>" + policyNumber + "</policyNumber>" +
                        "</ins:getPolicyDetails>"
        ));
    }

    public String getPolicyStatus(String policyNumber) throws Exception {
        return call("/HealthPolicySoapService", envelope(
                "<ins:getPolicyStatus>" +
                        "<policyNumber>" + policyNumber + "</policyNumber>" +
                        "</ins:getPolicyStatus>"
        ));
    }

    // ---------------- HEALTH ----------------

    public HttpResponse healthCheck() throws Exception {
        URL url = new URL(baseUrl + "/healthCheck"); // try /actuator/health if needed
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
        URL url = new URL(baseUrl + path);  // 🔥 KEY FIX HERE
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

    public static class HttpResponse {
        public int statusCode;
        public String body;

        public HttpResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
    }
}