package com.example.insurance.ejb;
import javax.ejb.Stateless;
@Stateless
public class PolicyServiceBean implements PolicyServiceRemote {
@Override
public String getPolicyStatus(String policyNumber) {
if (policyNumber == null || policyNumber.isBlank()) {
return "INVALID_POLICY";
}
if (policyNumber.startsWith("HLT")) {
return "ACTIVE";
}
return "PENDING";
}
}