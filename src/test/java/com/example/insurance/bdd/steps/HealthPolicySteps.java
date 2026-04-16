package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class HealthPolicySteps {

    private SoapClient client;
    private String lastPolicyDetailsResponse;
    private String lastPolicyStatusResponse;
    private SoapClient.HttpResponse lastHttpResponse;

    @Given("the insurance app is running")
    public void theInsuranceAppIsRunning() {
        String url = System.getenv("APP_BASE_URL");
        if (url == null || url.isEmpty()) {
            url = System.getProperty("APP_BASE_URL", "http://localhost:9080");
        }
        client = new SoapClient(url);
        System.out.println("[BDD] APP_BASE_URL = " + url);
    }

    @When("I request policy details for {string}")
    public void iRequestPolicyDetailsFor(String policyNumber) throws Exception {
        lastPolicyDetailsResponse = client.getPolicyDetails(policyNumber);
        System.out.println("[BDD] getPolicyDetails: " + lastPolicyDetailsResponse);
    }

    @When("I request policy status for {string}")
    public void iRequestPolicyStatusFor(String policyNumber) throws Exception {
        lastPolicyStatusResponse = client.getPolicyStatus(policyNumber);
        System.out.println("[BDD] getPolicyStatus: " + lastPolicyStatusResponse);
    }

    @When("I call the health check endpoint")
    public void iCallTheHealthCheckEndpoint() throws Exception {
        lastHttpResponse = client.healthCheck();
        System.out.println("[BDD] healthCheck HTTP " + lastHttpResponse.statusCode + ": " + lastHttpResponse.body);
    }

    @Then("the response contains {string}")
    public void theResponseContains(String expected) {
        Assert.assertNotNull("Policy details response was null", lastPolicyDetailsResponse);
        Assert.assertTrue(
            "Expected [" + expected + "] in:\n" + lastPolicyDetailsResponse,
            lastPolicyDetailsResponse.contains(expected));
    }

    @Then("the status response is {string}")
    public void theStatusResponseIs(String expected) {
        Assert.assertNotNull("Policy status response was null", lastPolicyStatusResponse);
        Assert.assertTrue(
            "Expected [" + expected + "] in:\n" + lastPolicyStatusResponse,
            lastPolicyStatusResponse.contains(expected));
    }

    @Then("the HTTP status is {int}")
    public void theHTTPStatusIs(int expectedCode) {
        Assert.assertEquals("HTTP status mismatch", expectedCode, lastHttpResponse.statusCode);
    }

    @Then("the response body contains {string}")
    public void theResponseBodyContains(String expected) {
        Assert.assertNotNull("HTTP body was null", lastHttpResponse.body);
        Assert.assertTrue(
            "Expected [" + expected + "] in body:\n" + lastHttpResponse.body,
            lastHttpResponse.body.contains(expected));
    }

    // Package-visible — used by ClaimsSteps via PicoContainer injection
    SoapClient getClient() {
        return client;
    }
}
