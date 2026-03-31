package com.example.insurance.ejb;

import com.example.insurance.model.Claim;

import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class ClaimServiceBean implements ClaimServiceRemote {

    private static final String JDBC_URL = "jdbc:h2:mem:insurance;DB_CLOSE_DELAY=-1";

    static {
        // initialize schema if not present
        try {
            Class.forName("org.h2.Driver");
            try (Connection c = DriverManager.getConnection(JDBC_URL);
                 Statement s = c.createStatement()) {
                s.execute("CREATE TABLE IF NOT EXISTS CLAIMS (CLAIM_ID VARCHAR(64) PRIMARY KEY, POLICY_NUMBER VARCHAR(64), CLAIM_STATUS VARCHAR(32), AMOUNT DOUBLE, CREATED_ON TIMESTAMP)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Claim createClaim(String policyNumber, double amount) {
        String id = "CLM-" + UUID.randomUUID().toString().substring(0, 8);
        Claim claim = new Claim(id, policyNumber, "RECEIVED", amount);
        try (Connection c = DriverManager.getConnection(JDBC_URL);
             PreparedStatement ps = c.prepareStatement("INSERT INTO CLAIMS (CLAIM_ID, POLICY_NUMBER, CLAIM_STATUS, AMOUNT, CREATED_ON) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, claim.getClaimId());
            ps.setString(2, claim.getPolicyNumber());
            ps.setString(3, claim.getClaimStatus());
            ps.setDouble(4, claim.getAmount());
            ps.setTimestamp(5, Timestamp.valueOf(claim.getCreatedOn()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claim;
    }

    @Override
    public Claim getClaim(String claimId) {
        try (Connection c = DriverManager.getConnection(JDBC_URL);
             PreparedStatement ps = c.prepareStatement("SELECT CLAIM_ID,POLICY_NUMBER,CLAIM_STATUS,AMOUNT,CREATED_ON FROM CLAIMS WHERE CLAIM_ID = ?")) {
            ps.setString(1, claimId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Claim claim = new Claim();
                    claim.setClaimId(rs.getString(1));
                    claim.setPolicyNumber(rs.getString(2));
                    claim.setClaimStatus(rs.getString(3));
                    claim.setAmount(rs.getDouble(4));
                    claim.setCreatedOn(rs.getTimestamp(5).toLocalDateTime());
                    return claim;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Claim> listClaimsForPolicy(String policyNumber) {
        List<Claim> list = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(JDBC_URL);
             PreparedStatement ps = c.prepareStatement("SELECT CLAIM_ID,POLICY_NUMBER,CLAIM_STATUS,AMOUNT,CREATED_ON FROM CLAIMS WHERE POLICY_NUMBER = ?")) {
            ps.setString(1, policyNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Claim claim = new Claim();
                    claim.setClaimId(rs.getString(1));
                    claim.setPolicyNumber(rs.getString(2));
                    claim.setClaimStatus(rs.getString(3));
                    claim.setAmount(rs.getDouble(4));
                    claim.setCreatedOn(rs.getTimestamp(5).toLocalDateTime());
                    list.add(claim);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
