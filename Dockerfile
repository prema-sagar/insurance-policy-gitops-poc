FROM ibmcom/websphere-liberty:latest

COPY build/libs/insurance-health-component.war /config/dropins/
COPY server.xml /config/

EXPOSE 9080 9443