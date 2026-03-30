package com.company.insurance.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class PolicyService {

    @WebMethod
    public String getPolicyDetails(String policyNumber) {
        return "Policy Details for: " + policyNumber;
    }
}
