package com.example.insurance.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://soap.insurance.example.com/")
public interface HealthPolicySoapService {

    @WebMethod
    PolicyDetailsResponse getPolicyDetails(
        @WebParam(name = "policyNumber") String policyNumber
    );

    @WebMethod
    String getPolicyStatus(
        @WebParam(name = "policyNumber") String policyNumber
    );
}