Feature: Health Policy SOAP Service
  As an insurance system consumer
  I want to query health policy details and status
  So that I can validate policy information is returned correctly

  Background:
    Given the insurance app is running

  Scenario: Get policy details for active policy POL1001
    When I request policy details for "POL1001"
    Then the response contains "Ravi Kumar"
    And the response contains "Health Insurance"
    And the response contains "ACTIVE"
    And the response contains "15000"

  Scenario: Get policy status for active policy POL1001
    When I request policy status for "POL1001"
    Then the status response is "ACTIVE"

  Scenario: Get policy status for expired policy POL1002
    When I request policy status for "POL1002"
    Then the status response is "EXPIRED"

  Scenario: Get policy status for pending policy POL1003
    When I request policy status for "POL1003"
    Then the status response is "PENDING"

  Scenario: Get policy details for unknown policy returns INVALID_POLICY
    When I request policy details for "UNKNOWN999"
    Then the response contains "INVALID_POLICY"
    And the response contains "0"

  Scenario: Health check endpoint returns 200
    When I call the health check endpoint
    Then the HTTP status is 200
    And the response body contains "Application is running"
