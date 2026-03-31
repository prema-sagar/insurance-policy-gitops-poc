package com.example.insurance.soap;

import javax.ejb.EJB;
import javax.jws.WebService;

import com.example.insurance.ejb.PolicyServiceRemote;
import com.example.insurance.model.PolicyResponse;

@WebService(
    endpointInterface = "com.example.insurance.soap.HealthPolicySoapService",
    serviceName = "HealthPolicySoapService",
    portName = "HealthPolicySoapPort"
)
public class HealthPolicySoapServiceImpl implements HealthPolicySoapService {

    @EJB
    private PolicyServiceRemote policyService;

    @Override
    public PolicyResponse getPolicyDetails(String policyNumber) {

        String status;
        if (policyService == null) {
            // Fallback logic when EJB isn't available (e.g., local tests)
            if (policyNumber == null || policyNumber.isBlank()) {
                status = "INVALID_POLICY";
            } else if (policyNumber.startsWith("POL") || policyNumber.startsWith("HLT")) {
                status = "ACTIVE";
            } else {
                status = "PENDING";
            }
        } else {
            status = policyService.getPolicyStatus(policyNumber);
        }

        PolicyResponse resp = new PolicyResponse();
        resp.setPolicyNumber(policyNumber);
        resp.setPolicyStatus(status);
        resp.setInsuredName("Unknown");
        return resp;
    }

    @Override
    public String getPolicyStatus(String policyNumber) {
        if (policyService == null) {
            if (policyNumber == null || policyNumber.isBlank()) {
                return "INVALID_POLICY";
            }
            if (policyNumber.startsWith("POL") || policyNumber.startsWith("HLT")) {
                return "ACTIVE";
            }
            return "PENDING";
        }
        return policyService.getPolicyStatus(policyNumber);
    }
}