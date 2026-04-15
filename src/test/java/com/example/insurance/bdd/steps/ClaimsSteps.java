package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

/**
 * Step definitions for health-policy.feature.
 *
 * This class initializes the shared SoapClient used across tests.
 */
public class HealthPolicySteps {

    private SoapClient client;
    private String lastResponse;

    public SoapClient getClient() {
        return client;
    }

    // ── Given ─────────────────────────────────────────────────────────────────

    @Given("the SOAP service is available")
    public void theSoapServiceIsAvailable() {
        // APP_BASE_URL comes from environment variable (GitHub Actions)
        String baseUrl = System.getenv("APP_BASE_URL");

        Assert.assertNotNull("APP_BASE_URL is not set", baseUrl);

        client = new SoapClient(baseUrl);
        System.out.println("[BDD] SOAP client initialized with URL: " + baseUrl);
    }

    // ── When ──────────────────────────────────────────────────────────────────

    @When("I create a health policy for {string} with coverage {double}")
    public void iCreateAHealthPolicy(String name, Double coverage) throws Exception {
        lastResponse = client.createPolicy(name, coverage);
        System.out.println("[BDD] createPolicy: " + lastResponse);
    }

    @When("I retrieve the policy by ID {string}")
    public void iRetrieveThePolicyById(String id) throws Exception {
        lastResponse = client.getPolicyById(id);
        System.out.println("[BDD] getPolicyById: " + lastResponse);
    }

    @When("I request all policies")
    public void iRequestAllPolicies() throws Exception {
        lastResponse = client.getAllPolicies();
        System.out.println("[BDD] getAllPolicies: " + lastResponse);
    }

    @When("I delete the policy with ID {string}")
    public void iDeleteThePolicy(String id) throws Exception {
        lastResponse = client.deletePolicy(id);
        System.out.println("[BDD] deletePolicy: " + lastResponse);
    }

    // ── Then / And ────────────────────────────────────────────────────────────

    // ✅ FIX: ONLY ONE annotation (removed duplicate @And)
    @Then("the response contains {string}")
    public void theResponseContains(String expected) {
        Assert.assertNotNull("Response was null", lastResponse);
        Assert.assertTrue(
                "Expected [" + expected + "] in:\n" + lastResponse,
                lastResponse.contains(expected)
        );
    }

    @And("the response is a valid SOAP response")
    public void theResponseIsValidSOAP() {
        Assert.assertNotNull("Response was null", lastResponse);
        Assert.assertTrue(
                "Expected SOAP Envelope in response:\n" + lastResponse,
                lastResponse.contains("Envelope")
        );
    }
}