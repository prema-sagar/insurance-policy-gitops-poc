package com.example.insurance.soap;

import com.example.insurance.model.PolicyResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "HealthPolicySoapService")
public interface HealthPolicySoapService {

    @WebMethod
    PolicyResponse getPolicyDetails(
        @WebParam(name = "policyNumber") String policyNumber
    );
}