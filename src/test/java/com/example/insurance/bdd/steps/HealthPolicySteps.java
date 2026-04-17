package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class HealthPolicySteps {

    private SoapClient client;
    private String response;
    private int statusCode = 200;

    @Given("the insurance app is running")
    public void running() {
        // build.gradle sets APP_BASE_URL via environment 'APP_BASE_URL', appUrl
        String baseUrl = System.getenv("APP_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = System.getProperty("APP_BASE_URL", "http://localhost:9080/insurance-health-component");
        }
        System.out.println("[BDD] APP_BASE_URL=" + baseUrl);
        client = new SoapClient(baseUrl);
    }

    @When("I request policy details for {string}")
    public void getDetails(String policy) {
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns=\"http://soap.insurance.example.com/\">" +
            "<soapenv:Body>" +
            "<ns:getPolicyDetails>" +
            "<policyNumber>" + policy + "</policyNumber>" +
            "</ns:getPolicyDetails>" +
            "</soapenv:Body>" +
            "</soapenv:Envelope>";
        response = client.call("HealthPolicySoapService", request);
    }

    @When("I request policy status for {string}")
    public void getStatus(String policy) {
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns=\"http://soap.insurance.example.com/\">" +
            "<soapenv:Body>" +
            "<ns:getPolicyStatus>" +
            "<policyNumber>" + policy + "</policyNumber>" +
            "</ns:getPolicyStatus>" +
            "</soapenv:Body>" +
            "</soapenv:Envelope>";
        response = client.call("HealthPolicySoapService", request);
    }

    @When("I call the health check endpoint")
    public void health() {
        // HealthCheckServlet is mapped to /health and returns "Application is running"
        response = client.callGet("health");
        statusCode = client.getLastStatusCode();
    }

    @Then("the response contains {string}")
    public void validate(String value) {
        assertTrue("Expected response to contain [" + value + "] but was:\n" + response,
                response.contains(value));
    }

    @Then("the status response is {string}")
    public void status(String value) {
        assertTrue("Expected status response to contain [" + value + "] but was:\n" + response,
                response.contains(value));
    }

    @Then("the HTTP status is {int}")
    public void http(int code) {
        assertEquals("Expected HTTP " + code + " but got " + statusCode, code, statusCode);
    }

    @Then("the response body contains {string}")
    public void body(String text) {
        assertTrue("Expected response body to contain [" + text + "] but was:\n" + response,
                response.contains(text));
    }
}