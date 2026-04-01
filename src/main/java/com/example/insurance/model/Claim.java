package com.example.insurance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

@XmlRootElement(name = "Claim")
@XmlAccessorType(XmlAccessType.FIELD)
public class Claim implements Serializable {

    private String claimId;

    private String policyNumber;

    private String claimStatus;

    private double amount;

    @XmlJavaTypeAdapter(com.example.insurance.model.LocalDateTimeAdapter.class)
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
