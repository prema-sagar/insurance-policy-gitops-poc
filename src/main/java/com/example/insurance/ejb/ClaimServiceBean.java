package com.example.insurance.ejb;

import com.example.insurance.model.Claim;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Stateless
public class ClaimServiceBean implements ClaimServiceRemote {

    @PersistenceContext(unitName = "insurancePU")
    private EntityManager em;

    @Override
    public Claim createClaim(String policyNumber, double amount) {
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("policyNumber must not be null or empty");
        }
        String id = "CLM-" + UUID.randomUUID().toString().substring(0, 8);
        Claim claim = new Claim(id, policyNumber, "RECEIVED", amount);
        em.persist(claim);
        return claim;
    }

    @Override
    public Claim getClaim(String claimId) {
        if (claimId == null || claimId.trim().isEmpty()) {
            return null;
        }
        return em.find(Claim.class, claimId);
    }

    @Override
    public List<Claim> listClaimsForPolicy(String policyNumber) {
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            return Collections.emptyList();
        }
        TypedQuery<Claim> q = em.createQuery(
                "SELECT c FROM Claim c WHERE c.policyNumber = :pn", Claim.class);
        q.setParameter("pn", policyNumber);
        return q.getResultList();
    }
}