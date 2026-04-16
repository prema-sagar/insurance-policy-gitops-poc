package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.utils.SoapClient;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class HealthPolicySteps {

    private SoapClient client;
    private String response;
    private int statusCode;

    private final String baseUrl = System.getenv().getOrDefault("BASE_URL",
            "http://16.170.133.24:30080/insurance-health-component");

    @Given("the insurance app is running")
    public void running() {
        client = new SoapClient(baseUrl);
    }

    @When("I request policy details for {string}")
    public void getDetails(String policyNumber) {

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<ins:getPolicyDetails>" +
                        "<policyNumber>" + policyNumber + "</policyNumber>" +
                        "</ins:getPolicyDetails>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.callSoapService("/HealthPolicySoapService", body);
        statusCode = client.getStatusCode();
    }

    @When("I request policy status for {string}")
    public void getStatus(String policyNumber) {

        String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:ins=\"http://soap.insurance.example.com/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<ins:getPolicyStatus>" +
                        "<policyNumber>" + policyNumber + "</policyNumber>" +
                        "</ins:getPolicyStatus>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        response = client.callSoapService("/HealthPolicySoapService", body);
        statusCode = client.getStatusCode();
    }

    @When("I call the health check endpoint")
    public void health() {
        // Reusing SOAP call as health check
        getStatus("POL1001");
    }

    @Then("the response contains {string}")
    public void validate(String text) {
        System.out.println(response);
        Assert.assertTrue(response.contains(text));
    }

    @Then("the status response is {string}")
    public void status(String expected) {
        Assert.assertTrue(response.contains(expected));
    }

    @Then("the HTTP status is {int}")
    public void http(int code) {
        Assert.assertEquals(code, statusCode);
    }

    @Then("the response body contains {string}")
    public void body(String expected) {
        Assert.assertTrue(response.contains(expected));
    }
}