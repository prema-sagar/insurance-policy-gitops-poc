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
        if (baseUrl == null) {
            baseUrl = "http://localhost:9080";
        }

        String context = System.getenv("APP_CONTEXT");
        if (context == null) {
            context = "insurance-health-component";
        }

        client = new SoapClient(baseUrl, context);

        System.out.println("[BDD] BASE_URL=" + baseUrl);
        System.out.println("[BDD] CONTEXT=" + context);
    }

    @When("I request policy details for {string}")
    public void iRequestPolicyDetailsFor(String policyNumber) throws Exception {
        lastPolicyDetailsResponse = client.getPolicyDetails(policyNumber);
    }

    @When("I request policy status for {string}")
    public void iRequestPolicyStatusFor(String policyNumber) throws Exception {
        lastPolicyStatusResponse = client.getPolicyStatus(policyNumber);
    }

    @When("I call the health check endpoint")
    public void iCallTheHealthCheckEndpoint() throws Exception {
        lastHttpResponse = client.healthCheck();
    }

    @Then("the response contains {string}")
    public void theResponseContains(String expected) {
        Assert.assertTrue(lastPolicyDetailsResponse.contains(expected));
    }

    @Then("the status response is {string}")
    public void theStatusResponseIs(String expected) {
        Assert.assertTrue(lastPolicyStatusResponse.contains(expected));
    }

    @Then("the HTTP status is {int}")
    public void theHTTPStatusIs(int expectedCode) {
        Assert.assertEquals(expectedCode, lastHttpResponse.statusCode);
    }

    @Then("the response body contains {string}")
    public void theResponseBodyContains(String expected) {
        Assert.assertTrue(lastHttpResponse.body.contains(expected));
    }

    SoapClient getClient() {
        return client;
    }
}