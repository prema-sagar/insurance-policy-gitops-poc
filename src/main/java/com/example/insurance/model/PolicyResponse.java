package com.example.insurance.model;

import java.io.Serializable;

public class PolicyResponse implements Serializable {

    private String policyNumber;
    private String policyStatus;

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }
}
