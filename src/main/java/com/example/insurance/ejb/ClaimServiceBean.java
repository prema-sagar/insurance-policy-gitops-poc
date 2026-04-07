@Override
public Claim createClaim(String policyNumber, double amount) {

    if (policyNumber == null || policyNumber.trim().isEmpty()) {
        policyNumber = "POLICY-DEFAULT";
    }

    String id = "CLM-" + UUID.randomUUID().toString().substring(0, 8);

    Claim claim = new Claim(id, policyNumber, "RECEIVED", amount);

    em.persist(claim);

    return claim;
}