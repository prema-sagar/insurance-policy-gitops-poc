package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.utils.SoapClient;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class ClaimsSteps {

    private SoapClient client;
    private String response;
    private String claimId;

    private final String baseUrl = System.getenv().getOrDefault("BASE_URL",
            "http://16.170.133.24:30080/insurance-health-component");

    @Given("the insurance app is running")
    public void running() {
        client = new SoapClient(baseUrl);
    }

    @When("I create a claim for policy {string} with amount {double}")
    public void createClaim(String policyNumber, double amount) {

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<ins:createClaim>" +
                        "<policyNumber>" + policyNumber + "</policyNumber>" +
                        "<amount>" + amount + "</amount>" +
                        "</ins:createClaim>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.callSoapService("/ClaimsSoapService", body);

        // extract claim ID
        claimId = response.replaceAll(".*<id>(.*?)</id>.*", "$1");
        System.out.println("[CLAIM ID] " + claimId);
    }

    @Given("a claim has been created for policy {string} with amount {double}")
    public void createClaimPre(String policyNumber, double amount) {
        createClaim(policyNumber, amount);
    }

    @When("I retrieve the claim by its ID")
    public void getClaim() {

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<ins:getClaimById>" +
                        "<claimId>" + claimId + "</claimId>" +
                        "</ins:getClaimById>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.callSoapService("/ClaimsSoapService", body);
    }

    @When("I request all claims")
    public void getAllClaims() {

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<ins:getAllClaims/>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.callSoapService("/ClaimsSoapService", body);
    }

    @When("I update the claim status to {string}")
    public void updateClaim(String status) {

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<ins:updateClaimStatus>" +
                        "<claimId>" + claimId + "</claimId>" +
                        "<status>" + status + "</status>" +
                        "</ins:updateClaimStatus>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.callSoapService("/ClaimsSoapService", body);
    }

    @When("I delete the claim")
    public void deleteClaim() {

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<ins:deleteClaim>" +
                        "<claimId>" + claimId + "</claimId>" +
                        "</ins:deleteClaim>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.callSoapService("/ClaimsSoapService", body);
    }

    @Then("the claim response contains {string}")
    public void validate(String text) {
        System.out.println(response);
        Assert.assertTrue(response.contains(text));
    }

    @Then("the all-claims response is a valid SOAP response")
    public void validateAll() {
        Assert.assertTrue(response.contains("<claims>"));
    }

    @Then("a claim ID is returned")
    public void checkId() {
        Assert.assertNotNull(claimId);
        Assert.assertFalse(claimId.isEmpty());
    }

    @Then("the delete response contains {string}")
    public void validateDelete(String text) {
        Assert.assertTrue(response.contains(text));
    }
}