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

        String baseUrl = System.getenv("APP_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = System.getProperty("APP_BASE_URL", "http://localhost:9080");
        }

        // ✅ NEW: context path (important fix)
        String contextPath = System.getenv("APP_CONTEXT");
        if (contextPath == null || contextPath.isEmpty()) {
            contextPath = "insurance-health-component";
        }

        client = new SoapClient(baseUrl, contextPath);

        System.out.println("[BDD] BASE_URL = " + baseUrl);
        System.out.println("[BDD] CONTEXT_PATH = " + contextPath);
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
        Assert.assertNotNull(lastPolicyDetailsResponse);
        Assert.assertTrue(lastPolicyDetailsResponse.contains(expected));
    }

    @Then("the status response is {string}")
    public void theStatusResponseIs(String expected) {
        Assert.assertNotNull(lastPolicyStatusResponse);
        Assert.assertTrue(lastPolicyStatusResponse.contains(expected));
    }

    @Then("the HTTP status is {int}")
    public void theHTTPStatusIs(int expectedCode) {
        Assert.assertEquals(expectedCode, lastHttpResponse.statusCode);
    }

    @Then("the response body contains {string}")
    public void theResponseBodyContains(String expected) {
        Assert.assertNotNull(lastHttpResponse.body);
        Assert.assertTrue(lastHttpResponse.body.contains(expected));
    }

    SoapClient getClient() {
        return client;
    }
}