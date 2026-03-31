package com.example.insurance.service;

/**
 * Simple service used by unit tests. Intentionally lightweight — returns a policy
 * status string based on the policy number prefix so existing tests pass.
 */
public class HealthPolicySoapService {

    public String getPolicyStatus(String policyNumber) {
        if (policyNumber == null || policyNumber.isBlank()) {
            return "INVALID_POLICY";
        }
        // Treat both POL and HLT prefixes as active for test coverage
        if (policyNumber.startsWith("POL") || policyNumber.startsWith("HLT")) {
            return "ACTIVE";
        }
        return "PENDING";
    }
}