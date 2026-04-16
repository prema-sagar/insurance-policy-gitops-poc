package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class ClaimsSteps {

    private SoapClient client;
    private String response;
    private String claimId;

    private void initClient() {

        if (client == null) {

            String baseUrl = System.getenv("BASE_URL");

            if (baseUrl == null || baseUrl.isEmpty()) {
                throw new RuntimeException("BASE_URL environment variable is not set");
            }

            System.out.println("[BDD] BASE_URL=" + baseUrl);

            client = new SoapClient(baseUrl);
        }
    }

    @Given("a claim has been created for policy {string} with amount {double}")
    public void createClaimPre(String policy, double amount) {
        initClient();
        createClaim(policy, amount);
    }

    @When("I create a claim for policy {string} with amount {double}")
    public void createClaim(String policy, double amount) {

        initClient();

        String request =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Body>" +
                        "<ns:createClaim>" +
                        "<policyNumber>" + policy + "</policyNumber>" +
                        "<amount>" + amount + "</amount>" +
                        "</ns:createClaim>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.call("ClaimsSoapService", request);

        claimId = extract(response, "<id>", "</id>");
        System.out.println("[CLAIM ID] " + claimId);
    }

    @When("I retrieve the claim by its ID")
    public void getClaim() {

        String request =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Body>" +
                        "<ns:getClaimById>" +
                        "<claimId>" + claimId + "</claimId>" +
                        "</ns:getClaimById>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.call("ClaimsSoapService", request);
    }

    @When("I request all claims")
    public void getAllClaims() {

        initClient();

        String request =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Body>" +
                        "<ns:getAllClaims/>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.call("ClaimsSoapService", request);
    }

    @When("I update the claim status to {string}")
    public void updateClaim(String status) {

        String request =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Body>" +
                        "<ns:updateClaimStatus>" +
                        "<claimId>" + claimId + "</claimId>" +
                        "<status>" + status + "</status>" +
                        "</ns:updateClaimStatus>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.call("ClaimsSoapService", request);
    }

    @When("I delete the claim")
    public void deleteClaim() {

        String request =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Body>" +
                        "<ns:deleteClaim>" +
                        "<claimId>" + claimId + "</claimId>" +
                        "</ns:deleteClaim>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.call("ClaimsSoapService", request);
    }

    @Then("the claim response contains {string}")
    public void validate(String value) {
        assertTrue(response.contains(value));
    }

    @Then("the all-claims response is a valid SOAP response")
    public void validateAll() {
        assertTrue(response.contains("<claims>"));
    }

    @Then("a claim ID is returned")
    public void checkId() {
        assertNotNull(claimId);
    }

    @Then("the delete response contains {string}")
    public void validateDelete(String value) {
        assertTrue(response.contains(value));
    }

    private String extract(String xml, String start, String end) {
        try {
            return xml.split(start)[1].split(end)[0];
        } catch (Exception e) {
            return null;
        }
    }
}