package com.example.insurance.ejb;

import com.example.insurance.model.Claim;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class ClaimServiceBean implements ClaimServiceRemote {

    // In-memory storage for demo (no database)
    private static final List<Claim> claims = new ArrayList<>();

    @Override
    public Claim createClaim(String policyNumber, double amount) {
        String id = "CLM-" + UUID.randomUUID().toString().substring(0, 8);
        Claim claim = new Claim(id, policyNumber, "RECEIVED", amount);
        claims.add(claim);
        return claim;
    }

    @Override
    public Claim getClaim(String claimId) {
        return claims.stream()
                .filter(c -> c.getClaimId().equals(claimId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Claim> listClaimsForPolicy(String policyNumber) {
        return claims.stream()
                .filter(c -> c.getPolicyNumber().equals(policyNumber))
                .toList();
    }
}
