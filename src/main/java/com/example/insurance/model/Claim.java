package com.example.insurance.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CLAIMS")
public class Claim implements Serializable {

    @Id
    @Column(name = "CLAIM_ID", length = 64)
    private String claimId;

    @Column(name = "POLICY_NUMBER", length = 64)
    private String policyNumber;

    @Column(name = "CLAIM_STATUS", length = 32)
    private String claimStatus;

    @Column(name = "AMOUNT")
    private double amount;

    @Column(name = "CREATED_ON")
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
