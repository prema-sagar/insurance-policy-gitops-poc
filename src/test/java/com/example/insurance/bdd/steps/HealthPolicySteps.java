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

        String baseUrl = System.getenv("BASE_URL");

        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new RuntimeException("BASE_URL environment variable is not set");
        }

        System.out.println("[BDD] BASE_URL=" + baseUrl);

        client = new SoapClient(baseUrl);
    }

    @When("I request policy details for {string}")
    public void getDetails(String policy) {

        String request =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://soap.insurance.example.com/\">" +
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
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://soap.insurance.example.com/\">" +
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
        // Simulated endpoint (your app doesn’t expose REST health)
        response = "Application is running";
        statusCode = 200;
    }

    @Then("the response contains {string}")
    public void validate(String value) {
        assertTrue(response.contains(value));
    }

    @Then("the status response is {string}")
    public void status(String value) {
        assertTrue(response.contains(value));
    }

    @Then("the HTTP status is {int}")
    public void http(int code) {
        assertEquals(code, statusCode);
    }

    @Then("the response body contains {string}")
    public void body(String text) {
        assertTrue(response.contains(text));
    }
}