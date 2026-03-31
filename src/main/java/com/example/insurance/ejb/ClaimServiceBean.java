package com.example.insurance.ejb;

import com.example.insurance.model.Claim;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Stateless
public class ClaimServiceBean implements ClaimServiceRemote {

    @PersistenceContext(unitName = "insurancePU")
    private EntityManager em;

    @Override
    public Claim createClaim(String policyNumber, double amount) {
        String id = "CLM-" + UUID.randomUUID().toString().substring(0, 8);
        Claim claim = new Claim(id, policyNumber, "RECEIVED", amount);
        em.persist(claim);
        return claim;
    }

    @Override
    public Claim getClaim(String claimId) {
        return em.find(Claim.class, claimId);
    }

    @Override
    public List<Claim> listClaimsForPolicy(String policyNumber) {
        TypedQuery<Claim> q = em.createQuery("SELECT c FROM Claim c WHERE c.policyNumber = :pn", Claim.class);
        q.setParameter("pn", policyNumber);
        return q.getResultList();
    }
}
