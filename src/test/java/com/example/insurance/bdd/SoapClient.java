package com.example.insurance.bdd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Minimal HTTP/SOAP client for BDD tests.
 * No generated stubs — sends raw SOAP envelopes so there are zero
 * compile-time dependencies on JAX-WS or the app's production classes.
 *
 * Endpoint paths from server.xml / @WebService / @WebServlet annotations:
 *   HealthPolicySoapService  →  /InsuranceApp/HealthPolicySoapService
 *   ClaimsSoapService        →  /InsuranceApp/ClaimsSoapService
 *   HealthCheckServlet       →  /InsuranceApp/health
 */
public class SoapClient {

    private final String baseUrl;

    public SoapClient(String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
    }

    // ── HealthPolicySoapService ───────────────────────────────────────────────

    public String getPolicyDetails(String policyNumber) throws Exception {
        String body =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:ins=\"http://soap.insurance.example.com/\">\n" +
            "  <soapenv:Header/>\n" +
            "  <soapenv:Body>\n" +
            "    <ins:getPolicyDetails>\n" +
            "      <ins:policyNumber>" + policyNumber + "</ins:policyNumber>\n" +
            "    </ins:getPolicyDetails>\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return post("/InsuranceApp/HealthPolicySoapService", body,
                    "\"http://soap.insurance.example.com/getPolicyDetails\"");
    }

    public String getPolicyStatus(String policyNumber) throws Exception {
        String body =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:ins=\"http://soap.insurance.example.com/\">\n" +
            "  <soapenv:Header/>\n" +
            "  <soapenv:Body>\n" +
            "    <ins:getPolicyStatus>\n" +
            "      <ins:policyNumber>" + policyNumber + "</ins:policyNumber>\n" +
            "    </ins:getPolicyStatus>\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return post("/InsuranceApp/HealthPolicySoapService", body,
                    "\"http://soap.insurance.example.com/getPolicyStatus\"");
    }

    // ── ClaimsSoapService — @WebParam names from ClaimsSoapService.java ───────

    public String createClaim(String policyNumber, double amount) throws Exception {
        String body =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:ins=\"http://soap.insurance.example.com/\">\n" +
            "  <soapenv:Header/>\n" +
            "  <soapenv:Body>\n" +
            "    <ins:createClaim>\n" +
            "      <ins:policyNumber>" + policyNumber + "</ins:policyNumber>\n" +
            "      <ins:amount>" + amount + "</ins:amount>\n" +
            "    </ins:createClaim>\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return post("/InsuranceApp/ClaimsSoapService", body,
                    "\"http://soap.insurance.example.com/createClaim\"");
    }

    public String getClaimById(String claimId) throws Exception {
        String body =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:ins=\"http://soap.insurance.example.com/\">\n" +
            "  <soapenv:Header/>\n" +
            "  <soapenv:Body>\n" +
            "    <ins:getClaimById>\n" +
            "      <ins:claimId>" + claimId + "</ins:claimId>\n" +
            "    </ins:getClaimById>\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return post("/InsuranceApp/ClaimsSoapService", body,
                    "\"http://soap.insurance.example.com/getClaimById\"");
    }

    public String getAllClaims() throws Exception {
        String body =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:ins=\"http://soap.insurance.example.com/\">\n" +
            "  <soapenv:Header/>\n" +
            "  <soapenv:Body>\n" +
            "    <ins:getAllClaims/>\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return post("/InsuranceApp/ClaimsSoapService", body,
                    "\"http://soap.insurance.example.com/getAllClaims\"");
    }

    public String updateClaimStatus(String claimId, String status) throws Exception {
        String body =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:ins=\"http://soap.insurance.example.com/\">\n" +
            "  <soapenv:Header/>\n" +
            "  <soapenv:Body>\n" +
            "    <ins:updateClaimStatus>\n" +
            "      <ins:claimId>" + claimId + "</ins:claimId>\n" +
            "      <ins:status>" + status + "</ins:status>\n" +
            "    </ins:updateClaimStatus>\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return post("/InsuranceApp/ClaimsSoapService", body,
                    "\"http://soap.insurance.example.com/updateClaimStatus\"");
    }

    public String deleteClaim(String claimId) throws Exception {
        String body =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:ins=\"http://soap.insurance.example.com/\">\n" +
            "  <soapenv:Header/>\n" +
            "  <soapenv:Body>\n" +
            "    <ins:deleteClaim>\n" +
            "      <ins:claimId>" + claimId + "</ins:claimId>\n" +
            "    </ins:deleteClaim>\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>";
        return post("/InsuranceApp/ClaimsSoapService", body,
                    "\"http://soap.insurance.example.com/deleteClaim\"");
    }

    // ── HealthCheckServlet (@WebServlet("/health")) ───────────────────────────

    public HttpResponse healthCheck() throws Exception {
        URL url = new URL(baseUrl + "/InsuranceApp/health");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);
        int status = conn.getResponseCode();
        String body = readStream(conn);
        conn.disconnect();
        return new HttpResponse(status, body);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private String post(String path, String envelope, String soapAction) throws Exception {
        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction", soapAction);
        conn.setConnectTimeout(15_000);
        conn.setReadTimeout(15_000);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(envelope.getBytes(StandardCharsets.UTF_8));
        }
        String response = readStream(conn);
        conn.disconnect();
        return response;
    }

    private String readStream(HttpURLConnection conn) throws Exception {
        int status = conn.getResponseCode();
        java.io.InputStream is = (status >= 400) ? conn.getErrorStream() : conn.getInputStream();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();
        return sb.toString();
    }

    public static class HttpResponse {
        public final int    statusCode;
        public final String body;
        public HttpResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body       = body;
        }
    }
}
