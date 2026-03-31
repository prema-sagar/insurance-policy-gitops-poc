package com.example.insurance.ejb;

import com.example.insurance.model.Claim;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ClaimServiceRemote {
    Claim createClaim(String policyNumber, double amount);
    Claim getClaim(String claimId);
    List<Claim> listClaimsForPolicy(String policyNumber);
}
