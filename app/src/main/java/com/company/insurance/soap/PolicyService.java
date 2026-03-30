package com.company.insurance.soap;

import javax.jws.WebService;
import javax.jws.WebMethod;

@WebService
public class PolicyService {

    @WebMethod
    public String getPolicy(String policyId) {
        return "Policy details for " + policyId;
    }

    @WebMethod
    public String healthCheck() {
        return "OK";
    }
}
