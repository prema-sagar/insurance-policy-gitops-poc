package com.example.insurance.soap;

import com.example.insurance.model.Claim;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "ClaimsSoapService")
public interface ClaimsSoapService {

    @WebMethod
    Claim createClaim(String policyNumber, double amount);

    @WebMethod
    Claim getClaim(String claimId);

    @WebMethod
    List<Claim> listClaimsForPolicy(String policyNumber);
}
