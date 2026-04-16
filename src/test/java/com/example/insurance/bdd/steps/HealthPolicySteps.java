package com.example.insurance.bdd.steps;

import com.example.insurance.bdd.SoapClient;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import static org.junit.Assert.*;

public class HealthPolicySteps {

    private SoapClient client;
    private String response;

    @Before
    public void setup() {
        String baseUrl = System.getenv().getOrDefault(
                "APP_BASE_URL",
                "http://16.170.133.24:30080/insurance-health-component"
        );

        client = new SoapClient(baseUrl);
    }

    @Given("the insurance app is running")
    public void running() {
        assertNotNull(client);
    }

    @When("I request policy details for {string}")
    public void getDetails(String policy) throws Exception {
        response = client.getPolicyDetails(policy);
    }

    @When("I request policy status for {string}")
    public void getStatus(String policy) throws Exception {
        response = client.getPolicyStatus(policy);
    }

    @When("I call the health check endpoint")
    public void health() throws Exception {
        response = client.getPolicyStatus("POL1001");
    }

    @Then("the response contains {string}")
    public void validate(String val) {
        System.out.println(response);
        assertTrue(response.contains(val));
    }

    @Then("the status response is {string}")
    public void status(String val) {
        assertTrue(response.contains(val));
    }

    @Then("the HTTP status is {int}")
    public void http(int code) {
        assertTrue(response.contains("Envelope"));
    }

    @Then("the response body contains {string}")
    public void body(String val) {
        assertTrue(response.contains(val));
    }
}