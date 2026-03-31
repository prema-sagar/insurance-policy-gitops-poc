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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "policyId='" + policyId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", policyType='" + policyType + '\'' +
                ", premiumAmount=" + premiumAmount +
                '}';
    }
}
