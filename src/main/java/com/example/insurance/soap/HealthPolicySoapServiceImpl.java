package com.example.insurance.soap;

import com.example.insurance.ejb.PolicyServiceBean;
import com.example.insurance.model.PolicyResponse;

import javax.jws.WebService;

@WebService(endpointInterface = "com.example.insurance.soap.HealthPolicySoapService")
public class HealthPolicySoapServiceImpl implements HealthPolicySoapService {

    private final PolicyServiceBean policyServiceBean = new PolicyServiceBean();

    @Override
    public PolicyResponse getPolicyDetails(String policyNumber) {
        PolicyResponse response = new PolicyResponse();
        response.setPolicyNumber(policyNumber);
        response.setPolicyStatus(policyServiceBean.getPolicyStatus(policyNumber));
        return response;
    }
}
