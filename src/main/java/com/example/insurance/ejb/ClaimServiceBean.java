package com.example.insurance.ejb;

import com.example.insurance.model.Claim;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Stateless
public class ClaimServiceBean implements ClaimServiceLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Claim createClaim(String policyNumber, double amount) {

        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            policyNumber = "POLICY-DEFAULT";
        }

        String id = "CLM-" + UUID.randomUUID().toString().substring(0, 8);

        Claim claim = new Claim();
        claim.setId(id);
        claim.setPolicyNumber(policyNumber);
        claim.setStatus("RECEIVED");
        claim.setAmount(amount);

        em.persist(claim);

        return claim;
    }

    @Override
    public Claim getClaimById(String claimId) {
        return em.find(Claim.class, claimId);
    }

    @Override
    public List<Claim> getAllClaims() {
        TypedQuery<Claim> query = em.createQuery(
            "SELECT c FROM Claim c",
            Claim.class
        );
        return query.getResultList();
    }

    @Override
    public Claim updateClaimStatus(String claimId, String status) {
        Claim claim = em.find(Claim.class, claimId);

        if (claim != null) {
            claim.setStatus(status);
            em.merge(claim);
        }

        return claim;
    }

    @Override
    public boolean deleteClaim(String claimId) {
        Claim claim = em.find(Claim.class, claimId);

        if (claim != null) {
            em.remove(claim);
            return true;
        }

        return false;
    }
}