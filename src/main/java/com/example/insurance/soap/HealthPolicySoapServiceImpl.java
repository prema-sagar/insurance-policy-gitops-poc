package com.example.insurance.soap;

import javax.jws.WebService;

@WebService(
    endpointInterface = "com.example.insurance.soap.HealthPolicySoapService",
    serviceName = "HealthPolicySoapService"
)
public class HealthPolicySoapServiceImpl implements HealthPolicySoapService {

    @Override
    public PolicyDetailsResponse getPolicyDetails(String policyNumber) {

        PolicyDetailsResponse response = new PolicyDetailsResponse();

        if ("POL1001".equalsIgnoreCase(policyNumber)) {
            response.setPolicyNumber("POL1001");
            response.setInsuredName("Ravi Kumar");
            response.setPolicyType("Health Insurance");
            response.setPolicyStatus("ACTIVE");
            response.setPremiumAmount(15000.0);
        } else {
            response.setPolicyNumber(policyNumber);
            response.setInsuredName("Unknown");
            response.setPolicyType("Unknown");
            response.setPolicyStatus("INVALID_POLICY");
            response.setPremiumAmount(0.0);
        }

        return response;
    }

    @Override
    public String getPolicyStatus(String policyNumber) {

        if ("POL1001".equalsIgnoreCase(policyNumber)) {
            return "ACTIVE";
        } else if ("POL1002".equalsIgnoreCase(policyNumber)) {
            return "EXPIRED";
        } else if ("POL1003".equalsIgnoreCase(policyNumber)) {
            return "PENDING";
        }

        return "INVALID_POLICY";
    }
}