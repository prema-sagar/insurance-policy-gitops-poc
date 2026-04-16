package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.*;
import org.junit.Assert;

import java.util.regex.*;

public class ClaimsSteps {

    private final HealthPolicySteps healthSteps;
    private String lastResponse;
    private String claimId;

    public ClaimsSteps(HealthPolicySteps healthSteps) {
        this.healthSteps = healthSteps;
    }

    private SoapClient client() {
        return healthSteps.getClient();
    }

    @When("I create a claim for policy {string} with amount {double}")
    public void createClaim(String policy, Double amount) throws Exception {
        lastResponse = client().createClaim(policy, amount);
        claimId = extract(lastResponse, "id");
    }

    @Then("the claim response contains {string}")
    public void validate(String expected) {
        Assert.assertTrue(lastResponse.contains(expected));
    }

    @Then("a claim ID is returned")
    public void checkId() {
        Assert.assertNotNull(claimId);
    }

    private String extract(String xml, String tag) {
        Matcher m = Pattern.compile("<" + tag + ">(.*?)</").matcher(xml);
        return m.find() ? m.group(1) : null;
    }
}