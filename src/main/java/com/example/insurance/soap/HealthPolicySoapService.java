package com.example.insurance.soap;

import com.example.insurance.model.PolicyResponse;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface HealthPolicySoapService {

    @WebMethod
    PolicyResponse getPolicyDetails(String policyNumber);
}
