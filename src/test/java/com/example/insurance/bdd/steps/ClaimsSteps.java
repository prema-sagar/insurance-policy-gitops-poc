package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import static org.junit.Assert.*;

public class ClaimsSteps {

    private SoapClient client;
    private String response;
    private String claimId;

    @Before
    public void setup() {
        String baseUrl = System.getenv().getOrDefault(
                "APP_BASE_URL",
                "http://16.170.133.24:30080/insurance-health-component"
        );

        client = new SoapClient(baseUrl);
    }

    @When("I create a claim for policy {string} with amount {double}")
    public void createClaim(String policy, double amount) throws Exception {
        response = client.createClaim(policy, amount);

        if (response.contains("<id>")) {
            claimId = response.split("<id>")[1].split("</id>")[0];
        }

        System.out.println("[CLAIM ID] " + claimId);
    }

    @Given("a claim has been created for policy {string} with amount {double}")
    public void createClaimPre(String policy, double amount) throws Exception {
        createClaim(policy, amount);
        assertNotNull(claimId);
    }

    @When("I retrieve the claim by its ID")
    public void getClaim() throws Exception {
        assertNotNull(claimId);

        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Body>" +
                "<tns:getClaimById>" +
                "<claimId>" + claimId + "</claimId>" +
                "</tns:getClaimById>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        response = client.callSoap("ClaimsSoapService", xml);
    }

    @When("I request all claims")
    public void getAllClaims() throws Exception {
        response = client.getAllClaims();
    }

    @When("I update the claim status to {string}")
    public void updateClaim(String status) throws Exception {
        assertNotNull(claimId);

        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Body>" +
                "<tns:updateClaimStatus>" +
                "<claimId>" + claimId + "</claimId>" +
                "<status>" + status + "</status>" +
                "</tns:updateClaimStatus>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        response = client.callSoap("ClaimsSoapService", xml);
    }

    @When("I delete the claim")
    public void deleteClaim() throws Exception {
        assertNotNull(claimId);

        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"http://soap.insurance.example.com/\">" +
                "<soapenv:Body>" +
                "<tns:deleteClaim>" +
                "<claimId>" + claimId + "</claimId>" +
                "</tns:deleteClaim>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        response = client.callSoap("ClaimsSoapService", xml);
    }

    @Then("the claim response contains {string}")
    public void validate(String value) {
        System.out.println(response);
        assertTrue(response.contains(value));
    }

    @Then("the all-claims response is a valid SOAP response")
    public void validateAll() {
        assertTrue(response.contains("Envelope"));
    }

    @Then("a claim ID is returned")
    public void checkId() {
        assertNotNull(claimId);
    }

    @Then("the delete response contains {string}")
    public void validateDelete(String val) {
        assertTrue(response.contains(val));
    }
}