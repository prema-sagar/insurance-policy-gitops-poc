package com.example.insurance.model;

public class Policy {

    private String policyId;
    private String customerName;
    private String policyType;
    private double premiumAmount;

    public Policy() {
    }

    public Policy(String policyId, String customerName, String policyType, double premiumAmount) {
        this.policyId = policyId;
        this.customerName = customerName;
        this.policyType = policyType;
        this.premiumAmount = premiumAmount;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }
}
