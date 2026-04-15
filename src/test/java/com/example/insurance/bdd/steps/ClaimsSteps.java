package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Step definitions for claims.feature.
 *
 * Uses PicoContainer to share the SoapClient instance already initialised
 * by HealthPolicySteps via the shared Background step.
 */
public class ClaimsSteps {

    private final HealthPolicySteps healthSteps;

    private String lastClaimResponse;
    private String currentClaimId;

    public ClaimsSteps(HealthPolicySteps healthSteps) {
        this.healthSteps = healthSteps;
    }

    private SoapClient client() {
        return healthSteps.getClient();
    }

    // ── Given ─────────────────────────────────────────────────────────────────

    @Given("a claim has been created for policy {string} with amount {double}")
    public void aClaimHasBeenCreatedForPolicyWithAmount(String policy, Double amount) throws Exception {
        lastClaimResponse = client().createClaim(policy, amount);
        currentClaimId = extractXmlValue(lastClaimResponse, "id");
        System.out.println("[BDD] pre-created claim id=" + currentClaimId);
    }

    // ── When ──────────────────────────────────────────────────────────────────

    @When("I create a claim for policy {string} with amount {double}")
    public void iCreateAClaimForPolicyWithAmount(String policy, Double amount) throws Exception {
        lastClaimResponse = client().createClaim(policy, amount);
        currentClaimId = extractXmlValue(lastClaimResponse, "id");
        System.out.println("[BDD] createClaim: " + lastClaimResponse);
    }

    @When("I retrieve the claim by its ID")
    public void iRetrieveTheClaimByItsID() throws Exception {
        Assert.assertNotNull("No claim ID from previous step", currentClaimId);
        lastClaimResponse = client().getClaimById(currentClaimId);
        System.out.println("[BDD] getClaimById: " + lastClaimResponse);
    }

    @When("I request all claims")
    public void iRequestAllClaims() throws Exception {
        lastClaimResponse = client().getAllClaims();
        System.out.println("[BDD] getAllClaims: " + lastClaimResponse);
    }

    @When("I update the claim status to {string}")
    public void iUpdateTheClaimStatusTo(String status) throws Exception {
        Assert.assertNotNull("No claim ID from previous step", currentClaimId);
        lastClaimResponse = client().updateClaimStatus(currentClaimId, status);
        System.out.println("[BDD] updateClaimStatus: " + lastClaimResponse);
    }

    @When("I delete the claim")
    public void iDeleteTheClaim() throws Exception {
        Assert.assertNotNull("No claim ID from previous step", currentClaimId);
        lastClaimResponse = client().deleteClaim(currentClaimId);
        System.out.println("[BDD] deleteClaim: " + lastClaimResponse);
    }

    // ── Then / And ────────────────────────────────────────────────────────────

    @Then("the claim response contains {string}")
    public void theClaimResponseContains(String expected) {
        Assert.assertNotNull("Claim response was null", lastClaimResponse);
        Assert.assertTrue(
                "Expected [" + expected + "] in:\n" + lastClaimResponse,
                lastClaimResponse.contains(expected)
        );
    }

    @And("a claim ID is returned")
    public void aClaimIDIsReturned() {
        Assert.assertNotNull("claimId was null in response:\n" + lastClaimResponse, currentClaimId);
        Assert.assertFalse("claimId was empty", currentClaimId.trim().isEmpty());
    }

    @Then("the all-claims response is a valid SOAP response")
    public void theAllClaimsResponseIsAValidSOAPResponse() {
        Assert.assertNotNull("getAllClaims response was null", lastClaimResponse);
        Assert.assertTrue(
                "Expected SOAP Envelope in response:\n" + lastClaimResponse,
                lastClaimResponse.contains("Envelope")
        );
    }

    @Then("the delete response contains {string}")
    public void theDeleteResponseContains(String expected) {
        Assert.assertNotNull("Delete response was null", lastClaimResponse);
        Assert.assertTrue(
                "Expected [" + expected + "] in:\n" + lastClaimResponse,
                lastClaimResponse.contains(expected)
        );
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private String extractXmlValue(String xml, String tag) {
        if (xml == null) return null;
        Matcher m = Pattern.compile("<(?:[^:>]+:)?" + tag + ">([^<]+)<").matcher(xml);
        return m.find() ? m.group(1).trim() : null;
    }
}