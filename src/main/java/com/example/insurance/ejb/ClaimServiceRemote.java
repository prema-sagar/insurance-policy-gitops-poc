package com.example.insurance.ejb;

import com.example.insurance.model.Claim;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface ClaimServiceRemote {

    Claim createClaim(String policyNumber, double amount);

    Claim getClaimById(String claimId);

    List<Claim> getAllClaims();

    Claim updateClaimStatus(String claimId, String status);

    boolean deleteClaim(String claimId);
}