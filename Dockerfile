FROM ibmcom/websphere-liberty:latest

# Remove any pre-existing copies of the application in the server dropins
# to avoid "It is not possible to start two applications called ..." errors
RUN rm -f /opt/ibm/wlp/usr/servers/defaultServer/dropins/insurance-health-component* || true

# Copy server configuration and application WAR into the image
COPY server.xml /config/
COPY build/libs/insurance-health-component.war /config/dropins/

EXPOSE 9080 9443