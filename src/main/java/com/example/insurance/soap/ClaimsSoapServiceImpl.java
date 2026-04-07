package com.example.insurance.soap;

import com.example.insurance.ejb.ClaimServiceRemote;
import com.example.insurance.model.Claim;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.List;

@WebService(
    serviceName = "ClaimsSoapService",
    portName = "ClaimsSoapServicePort",
    endpointInterface = "com.example.insurance.soap.ClaimsSoapService",
    targetNamespace = "http://soap.insurance.example.com/"
)
public class ClaimsSoapServiceImpl implements ClaimsSoapService {

    @EJB
    private ClaimServiceRemote claimService;

    @Override
    public Claim createClaim(String policyNumber, double amount) {
        return claimService.createClaim(policyNumber, amount);
    }

    @Override
    public Claim getClaimById(String claimId) {
        return claimService.getClaimById(claimId);
    }

    @Override
    public List<Claim> getAllClaims() {
        return claimService.getAllClaims();
    }

    @Override
    public Claim updateClaimStatus(String claimId, String status) {
        return claimService.updateClaimStatus(claimId, status);
    }

    @Override
    public boolean deleteClaim(String claimId) {
        return claimService.deleteClaim(claimId);
    }
}