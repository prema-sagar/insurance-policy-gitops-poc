package com.example.insurance.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class HealthPolicySoapService {

    @WebMethod
    public String getPolicyStatus(String policyId) {
        return "Policy ID " + policyId + " is ACTIVE";
    }

    @WebMethod
    public double calculatePremium(int age, String planType) {
        if ("GOLD".equalsIgnoreCase(planType)) {
            return age * 150;
        } else if ("SILVER".equalsIgnoreCase(planType)) {
            return age * 100;
        }
        return age * 75;
    }
}
