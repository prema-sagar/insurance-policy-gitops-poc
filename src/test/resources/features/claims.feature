Feature: Claims SOAP Service
  As an insurance claims processor
  I want to create, retrieve, update and delete claims
  So that the full claim lifecycle works correctly after deployment

  Background:
    Given the insurance app is running

  Scenario: Create a new claim
    When I create a claim for policy "POL1001" with amount 5000.0
    Then the claim response contains "POL1001"
    And the claim response contains "5000"
    And a claim ID is returned

  Scenario: Retrieve a claim by ID
    Given a claim has been created for policy "POL1001" with amount 2500.0
    When I retrieve the claim by its ID
    Then the claim response contains "POL1001"
    And the claim response contains "2500"

  Scenario: Get all claims returns a response
    When I request all claims
    Then the all-claims response is a valid SOAP response

  Scenario: Update claim status to APPROVED
    Given a claim has been created for policy "POL1001" with amount 1000.0
    When I update the claim status to "APPROVED"
    Then the claim response contains "APPROVED"

  Scenario: Delete a claim
    Given a claim has been created for policy "POL1001" with amount 500.0
    When I delete the claim
    Then the delete response contains "true"
