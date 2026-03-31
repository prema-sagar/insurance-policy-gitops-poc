FROM websphere-liberty:latest

COPY --chown=1001:0 build/libs/insurance-health-component.war /config/dropins/
COPY --chown=1001:0 server.xml /config/

