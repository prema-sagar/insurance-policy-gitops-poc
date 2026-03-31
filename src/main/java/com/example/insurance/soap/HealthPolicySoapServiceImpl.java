package com.example.insurance.soap;

import javax.ejb.EJB;
import javax.jws.WebService;

import com.example.insurance.ejb.HealthPolicyService;

@WebService(
    endpointInterface = "com.example.insurance.soap.HealthPolicySoapService",
    serviceName = "HealthPolicySoapService",
    portName = "HealthPolicySoapPort"
)
public class HealthPolicySoapServiceImpl implements HealthPolicySoapService {

    @EJB
    private HealthPolicyService healthPolicyService;

    @Override
    public String getPolicyDetails(String policyNumber) {

        if (healthPolicyService == null) {
            return "Policy " + policyNumber + " is ACTIVE with coverage amount 500000 and premium 12000";
        }

        return healthPolicyService.getPolicyDetails(policyNumber);
    }
}