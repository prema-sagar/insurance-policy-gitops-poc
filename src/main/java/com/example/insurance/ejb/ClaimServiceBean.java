package com.example.insurance.ejb;

import com.example.insurance.model.Claim;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Stateless
public class ClaimServiceBean {

    @PersistenceContext
    private EntityManager em;

    public Claim createClaim(String policyNumber, double amount) {

        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            policyNumber = "POLICY-DEFAULT";
        }

        String id = "CLM-" + UUID.randomUUID().toString().substring(0, 8);

        Claim claim = new Claim(id, policyNumber, "RECEIVED", amount);

        em.persist(claim);

        return claim;
    }
}