package com.example.insurance.ejb;

import com.example.insurance.model.Claim;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ClaimServiceRemote {
    Claim createClaim(String policyNumber, double amount);
    Claim getClaim(String claimId);
    List<Claim> listClaimsForPolicy(String policyNumber);
}
