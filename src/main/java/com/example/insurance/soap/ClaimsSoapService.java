package com.example.insurance.soap;

import com.example.insurance.model.Claim;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ClaimsSoapService {

    @WebMethod
    Claim createClaim(String policyNumber, double amount);

    @WebMethod
    Claim getClaimById(String claimId);

    @WebMethod
    List<Claim> getAllClaims();

    @WebMethod
    Claim updateClaimStatus(String claimId, String status);

    @WebMethod
    boolean deleteClaim(String claimId);
}