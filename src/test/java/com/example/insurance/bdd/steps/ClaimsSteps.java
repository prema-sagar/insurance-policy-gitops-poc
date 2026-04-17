package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class ClaimsSteps {

    private SoapClient client;
    private String response;
    private String claimId;

    /**
     * ClaimsSteps does NOT have its own @Given("the insurance app is running").
     * That step is handled by HealthPolicySteps (shared glue).
     * We grab the client lazily using the same APP_BASE_URL logic.
     */
    private void initClient() {
        if (client == null) {
            String baseUrl = System.getenv("APP_BASE_URL");
            if (baseUrl == null || baseUrl.isEmpty()) {
                baseUrl = System.getProperty("APP_BASE_URL", "http://localhost:9080/insurance-health-component");
            }
            System.out.println("[BDD] ClaimsSteps APP_BASE_URL=" + baseUrl);
            client = new SoapClient(baseUrl);
        }
    }

    // ── Pre-condition used in Background (claims.feature has Background: Given the insurance app is running)
    // That step resolves to HealthPolicySteps.running() which sets the base URL there.
    // ClaimsSteps needs its own client initialised before any When step.

    @Given("a claim has been created for policy {string} with amount {double}")
    public void createClaimPre(String policy, double amount) {
        initClient();
        createClaim(policy, amount);
    }

    @When("I create a claim for policy {string} with amount {double}")
    public void createClaim(String policy, double amount) {
        initClient();
        // ClaimServiceBean.createClaim() sets id = "CLM-" + 8-char UUID, status = "RECEIVED"
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns=\"http://soap.insurance.example.com/\">" +
            "<soapenv:Body>" +
            "<ns:createClaim>" +
            "<policyNumber>" + policy + "</policyNumber>" +
            "<amount>" + amount + "</amount>" +
            "</ns:createClaim>" +
            "</soapenv:Body>" +
            "</soapenv:Envelope>";

        response = client.call("ClaimsSoapService", request);

        // SOAP wraps the Claim object: <claim><id>CLM-xxxxx</id>...
        claimId = extract(response, "<id>", "</id>");
        System.out.println("[CLAIM ID] " + claimId);
    }

    @When("I retrieve the claim by its ID")
    public void getClaim() {
        initClient();
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns=\"http://soap.insurance.example.com/\">" +
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
        // getAllClaims() returns List<Claim>; SOAP wraps each item with <claims> per @WebResult(name="claims")
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns=\"http://soap.insurance.example.com/\">" +
            "<soapenv:Body>" +
            "<ns:getAllClaims/>" +
            "</soapenv:Body>" +
            "</soapenv:Envelope>";
        response = client.call("ClaimsSoapService", request);
    }

    @When("I update the claim status to {string}")
    public void updateClaim(String status) {
        initClient();
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns=\"http://soap.insurance.example.com/\">" +
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
        initClient();
        // deleteClaim returns boolean; SOAP wraps as <deleted>true</deleted> or <deleted>false</deleted>
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns=\"http://soap.insurance.example.com/\">" +
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
        assertTrue("Expected claim response to contain [" + value + "] but was:\n" + response,
                response.contains(value));
    }

    @Then("the all-claims response is a valid SOAP response")
    public void validateAll() {
        // getAllClaims wraps result elements as <claims>; an empty list still returns a valid Envelope
        assertTrue("Expected valid SOAP envelope in all-claims response but was:\n" + response,
                response.contains("Envelope"));
    }

    @Then("a claim ID is returned")
    public void checkId() {
        assertNotNull("Expected a claim ID (CLM-xxx) to be present in response:\n" + response, claimId);
        assertTrue("Expected claim ID to start with CLM- but was: " + claimId,
                claimId.startsWith("CLM-"));
    }

    @Then("the delete response contains {string}")
    public void validateDelete(String value) {
        // deleteClaim returns boolean; SOAP serialises as <deleted>true</deleted>
        assertTrue("Expected delete response to contain [" + value + "] but was:\n" + response,
                response.contains(value));
    }

    private String extract(String xml, String start, String end) {
        try {
            return xml.split(start)[1].split(end)[0];
        } catch (Exception e) {
            return null;
        }
    }
}