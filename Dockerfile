FROM ibmcom/websphere-liberty:latest

# Remove any pre-existing copies of the application in the server dropins
# to avoid "It is not possible to start two applications called ..." errors
RUN rm -f /opt/ibm/wlp/usr/servers/defaultServer/dropins/insurance-health-component* || true

COPY build/libs/insurance-health-component.war /config/dropins/
COPY server.xml /config/

EXPOSE 9080 9443