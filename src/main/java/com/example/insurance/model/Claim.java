package com.example.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Claim implements Serializable {

    private String claimId;
    private String policyNumber;
    private String claimStatus;
    private double amount;
    private LocalDateTime createdOn;

    public Claim() {
    }

    public Claim(String claimId, String policyNumber, String claimStatus, double amount) {
        this.claimId = claimId;
        this.policyNumber = policyNumber;
        this.claimStatus = claimStatus;
        this.amount = amount;
        this.createdOn = LocalDateTime.now();
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
