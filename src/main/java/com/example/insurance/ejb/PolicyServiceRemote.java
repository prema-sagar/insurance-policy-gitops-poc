package com.example.insurance.ejb;
import javax.ejb.Local;

@Local
public interface PolicyServiceRemote {
	String getPolicyStatus(String policyNumber);
}