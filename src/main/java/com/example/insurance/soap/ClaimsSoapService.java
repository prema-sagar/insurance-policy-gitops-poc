package com.example.insurance.soap;

import com.example.insurance.model.Claim;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService(
    name = "ClaimsSoapService",
    targetNamespace = "http://soap.insurance.example.com/"
)
public interface ClaimsSoapService {

    @WebMethod(operationName = "createClaim")
    @WebResult(name = "claim")
    Claim createClaim(
        @WebParam(name = "policyNumber") String policyNumber,
        @WebParam(name = "amount") double amount
    );

    @WebMethod(operationName = "getClaimById")
    @WebResult(name = "claim")
    Claim getClaimById(
        @WebParam(name = "claimId") String claimId
    );

    @WebMethod(operationName = "getAllClaims")
    @WebResult(name = "claims")
    List<Claim> getAllClaims();

    @WebMethod(operationName = "updateClaimStatus")
    @WebResult(name = "claim")
    Claim updateClaimStatus(
        @WebParam(name = "claimId") String claimId,
        @WebParam(name = "status") String status
    );

    @WebMethod(operationName = "deleteClaim")
    @WebResult(name = "deleted")
    boolean deleteClaim(
        @WebParam(name = "claimId") String claimId
    );
}