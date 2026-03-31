package com.example.insurance.ejb;
import javax.ejb.Remote;
@Remote
public interface PolicyServiceRemote {
String getPolicyStatus(String policyNumber);
}