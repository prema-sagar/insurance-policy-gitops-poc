# Service endpoints (WSDL and SOAP endpoints)

This file lists the SOAP WSDL URLs and the operations available in this POC application.

Base application URL (when running locally with container port mapped to host 9080):
- http://localhost:9080/insurance-health-component/

WSDL URLs
- HealthPolicy SOAP WSDL
  - http://localhost:9080/insurance-health-component/HealthPolicySoapService?wsdl
  - Operations available on this service:
    - getPolicyDetails(String policyNumber) -> PolicyResponse
    - getPolicyStatus(String policyNumber) -> String

- Claims SOAP WSDL
  - http://localhost:9080/insurance-health-component/ClaimsSoapService?wsdl
  - Operations available on this service:
    - createClaim(String policyNumber, double amount) -> Claim
    - getClaim(String claimId) -> Claim
    - listClaimsForPolicy(String policyNumber) -> List<Claim>

Notes
- Each SOAP service exposes a WSDL describing the operations for that service; the WSDL URL is service-specific (see URLs above).
- All operations exposed by a given service share the same endpoint URL (the URL used to POST SOAP requests). For example, both `getPolicyDetails` and `getPolicyStatus` are called by POSTing SOAP to `http://localhost:9080/insurance-health-component/HealthPolicySoapService`.
- If you need a single consolidated WSDL that contains all services, you can either import the separate WSDLs into a composite WSDL or provide an API gateway that aggregates them — this repo currently exposes separate WSDLs per service.
- For SOAP 1.1 use Content-Type `text/xml; charset=utf-8` and (optionally) an empty `SOAPAction` header. For SOAP 1.2 use `application/soap+xml; charset=utf-8`.

Quick test curl (HealthPolicy getPolicyStatus example):

curl -X POST "http://localhost:9080/insurance-health-component/HealthPolicySoapService" \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H 'SOAPAction: ""' \
  --data-binary @requests/request-soap11.xml

This file is intended for demo-readiness and can be included in your management presentation.
