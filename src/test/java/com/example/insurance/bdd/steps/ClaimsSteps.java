package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.*;
import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Given("a claim has been created for policy {string} with amount {double}")
    public void aClaimHasBeenCreated(String policy, Double amount) throws Exception {
        lastClaimResponse = client().createClaim(policy, amount);
        currentClaimId = extractXmlValue(lastClaimResponse, "id");
        System.out.println("[BDD] pre-created claim id=" + currentClaimId);
    }

    @When("I create a claim for policy {string} with amount {double}")
    public void createClaim(String policy, Double amount) throws Exception {
        lastClaimResponse = client().createClaim(policy, amount);
        currentClaimId = extractXmlValue(lastClaimResponse, "id");
        System.out.println("[BDD] createClaim: " + lastClaimResponse);
    }

    @When("I retrieve the claim by its ID")
    public void getClaim() throws Exception {
        Assert.assertNotNull("No claim ID from previous step", currentClaimId);
        lastClaimResponse = client().getClaimById(currentClaimId);
        System.out.println("[BDD] getClaimById: " + lastClaimResponse);
    }

    @When("I request all claims")
    public void getAllClaims() throws Exception {
        lastClaimResponse = client().getAllClaims();
        System.out.println("[BDD] getAllClaims: " + lastClaimResponse);
    }

    @When("I update the claim status to {string}")
    public void updateClaim(String status) throws Exception {
        Assert.assertNotNull("No claim ID from previous step", currentClaimId);
        lastClaimResponse = client().updateClaimStatus(currentClaimId, status);
        System.out.println("[BDD] updateClaimStatus: " + lastClaimResponse);
    }

    @When("I delete the claim")
    public void deleteClaim() throws Exception {
        Assert.assertNotNull("No claim ID from previous step", currentClaimId);
        lastClaimResponse = client().deleteClaim(currentClaimId);
        System.out.println("[BDD] deleteClaim: " + lastClaimResponse);
    }

    @Then("the claim response contains {string}")
    public void validateResponse(String expected) {
        Assert.assertNotNull(lastClaimResponse);
        Assert.assertTrue(lastClaimResponse.contains(expected));
    }

    @Then("a claim ID is returned")
    public void validateClaimId() {
        Assert.assertNotNull(currentClaimId);
        Assert.assertFalse(currentClaimId.trim().isEmpty());
    }

    @Then("the all-claims response is a valid SOAP response")
    public void validateAllClaimsResponse() {
        Assert.assertNotNull(lastClaimResponse);
        Assert.assertTrue(lastClaimResponse.contains("Envelope"));
    }

    @Then("the delete response contains {string}")
    public void validateDeleteResponse(String expected) {
        Assert.assertNotNull(lastClaimResponse);
        Assert.assertTrue(lastClaimResponse.contains(expected));
    }

    private String extractXmlValue(String xml, String tag) {
        if (xml == null) return null;
        Matcher m = Pattern.compile("<(?:[^:>]+:)?" + tag + ">([^<]+)<").matcher(xml);
        return m.find() ? m.group(1).trim() : null;
    }
}