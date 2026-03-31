package com.example.insurance.soap;

import com.example.insurance.ejb.ClaimServiceRemote;
import com.example.insurance.model.Claim;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.List;

@WebService(
        endpointInterface = "com.example.insurance.soap.ClaimsSoapService",
        serviceName = "ClaimsSoapService",
        portName = "ClaimsSoapPort"
)
public class ClaimsSoapServiceImpl implements ClaimsSoapService {

    @EJB
    private ClaimServiceRemote claimService;

    @Override
    public Claim createClaim(String policyNumber, double amount) {
        if (claimService == null) {
            // fallback: create simple claim instance without persistence
            return new Claim("CLM-FALLBACK-" + System.currentTimeMillis(), policyNumber, "RECEIVED", amount);
        }
        return claimService.createClaim(policyNumber, amount);
    }

    @Override
    public Claim getClaim(String claimId) {
        if (claimService == null) {
            return null;
        }
        return claimService.getClaim(claimId);
    }

    @Override
    public List<Claim> listClaimsForPolicy(String policyNumber) {
        if (claimService == null) {
            return java.util.Collections.emptyList();
        }
        return claimService.listClaimsForPolicy(policyNumber);
    }
}
