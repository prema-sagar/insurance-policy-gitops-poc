package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.*;
import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClaimsSteps {

    private SoapClient client = new SoapClient(); // direct init

    private String lastClaimResponse;
    private String currentClaimId;

    // ── GIVEN ─────────────────────────────────────

    @Given("a claim has been created for policy {string} with amount {double}")
    public void aClaimHasBeenCreated(String policy, Double amount) throws Exception {
        lastClaimResponse = client.createClaim(policy, amount);
        currentClaimId = extractXmlValue(lastClaimResponse, "id");
    }

    // ── WHEN ─────────────────────────────────────

    @When("I create a claim for policy {string} with amount {double}")
    public void createClaim(String policy, Double amount) throws Exception {
        lastClaimResponse = client.createClaim(policy, amount);
        currentClaimId = extractXmlValue(lastClaimResponse, "id");
    }

    @When("I retrieve the claim by its ID")
    public void getClaim() throws Exception {
        lastClaimResponse = client.getClaimById(currentClaimId);
    }

    @When("I request all claims")
    public void getAllClaims() throws Exception {
        lastClaimResponse = client.getAllClaims();
    }

    @When("I update the claim status to {string}")
    public void updateClaim(String status) throws Exception {
        lastClaimResponse = client.updateClaimStatus(currentClaimId, status);
    }

    @When("I delete the claim")
    public void deleteClaim() throws Exception {
        lastClaimResponse = client.deleteClaim(currentClaimId);
    }

    // ── THEN ─────────────────────────────────────

    @Then("the claim response contains {string}")
    public void validateResponse(String expected) {
        Assert.assertTrue(lastClaimResponse.contains(expected));
    }

    @And("a claim ID is returned")
    public void validateClaimId() {
        Assert.assertNotNull(currentClaimId);
    }

    // ── HELPER ───────────────────────────────────

    private String extractXmlValue(String xml, String tag) {
        if (xml == null) return null;
        Matcher m = Pattern.compile("<(?:[^:>]+:)?" + tag + ">([^<]+)<").matcher(xml);
        return m.find() ? m.group(1).trim() : null;
    }
}