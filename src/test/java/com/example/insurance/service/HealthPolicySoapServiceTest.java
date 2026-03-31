package com.example.insurance.service;

import org.junit.Assert;
import org.junit.Test;

public class HealthPolicySoapServiceTest {

    @Test
    public void testGetPolicyStatus() {
        HealthPolicySoapService service = new HealthPolicySoapService();
        String response = service.getPolicyStatus("POL123");
        Assert.assertTrue(response.contains("ACTIVE"));
    }
}
