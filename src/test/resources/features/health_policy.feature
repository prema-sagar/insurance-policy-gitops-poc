Feature: Health Policy SOAP Service

  Scenario: Get policy details for active policy POL1001
    Given the insurance app is running
    When I request policy details for "POL1001"
    Then the response contains "Ravi Kumar"
    And the response contains "Health Insurance"
    And the response contains "ACTIVE"
    And the response contains "15000"

  Scenario: Get policy status for active policy POL1001
    Given the insurance app is running
    When I request policy status for "POL1001"
    Then the status response is "ACTIVE"

  Scenario: Get policy status for expired policy POL1002
    Given the insurance app is running
    When I request policy status for "POL1002"
    Then the status response is "EXPIRED"

  Scenario: Get policy status for pending policy POL1003
    Given the insurance app is running
    When I request policy status for "POL1003"
    Then the status response is "PENDING"

  Scenario: Get policy details for unknown policy returns INVALID_POLICY
    Given the insurance app is running
    When I request policy details for "UNKNOWN999"
    Then the response contains "INVALID_POLICY"
    And the response contains "0"

  Scenario: Health check endpoint returns 200
    Given the insurance app is running
    When I call the health check endpoint
    Then the HTTP status is 200
    And the response body contains "Application is running"
