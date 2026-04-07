package com.example.insurance.ejb;

import com.example.insurance.model.Claim;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ClaimServiceLocal {

    Claim createClaim(String policyNumber, double amount);

    Claim getClaimById(String claimId);

    List<Claim> getAllClaims();

    Claim updateClaimStatus(String claimId, String status);

    boolean deleteClaim(String claimId);
}