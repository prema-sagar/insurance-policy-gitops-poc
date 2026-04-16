package com.example.insurance.bdd;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapClient {

    private final String baseUrl;

    public SoapClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String callSoap(String endpoint, String xml) throws Exception {

        String fullUrl = baseUrl + "/" + endpoint;
        System.out.println("[SOAP URL] " + fullUrl);

        URL url = new URL(fullUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(xml.getBytes());
        }

        int status = conn.getResponseCode();

        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        String res = response.toString();
        System.out.println("[SOAP RESPONSE] " + res);

        return res;
    }

    // -------- CLAIMS --------

    public String createClaim(String policy, double amount) throws Exception {
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Body>" +
                "<tns:createClaim>" +
                "<policyNumber>" + policy + "</policyNumber>" +
                "<amount>" + amount + "</amount>" +
                "</tns:createClaim>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoap("ClaimsSoapService", xml);
    }

    public String getAllClaims() throws Exception {
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Body>" +
                "<tns:getAllClaims/>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoap("ClaimsSoapService", xml);
    }

    // -------- POLICY --------

    public String getPolicyDetails(String policy) throws Exception {
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Body>" +
                "<tns:getPolicyDetails>" +
                "<policyNumber>" + policy + "</policyNumber>" +
                "</tns:getPolicyDetails>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoap("HealthPolicySoapService", xml);
    }

    public String getPolicyStatus(String policy) throws Exception {
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Body>" +
                "<tns:getPolicyStatus>" +
                "<policyNumber>" + policy + "</policyNumber>" +
                "</tns:getPolicyStatus>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        return callSoap("HealthPolicySoapService", xml);
    }
}