package com.example.insurance.ejb;

import javax.ejb.Stateless;

@Stateless
public class PolicyServiceBean implements PolicyServiceRemote {

    @Override
    public String getPolicyStatus(String policyNumber) {
        if (policyNumber == null || policyNumber.isEmpty()) {
            return "INVALID_POLICY";
        }

        return "ACTIVE_POLICY_FOR_" + policyNumber;
    }
}
