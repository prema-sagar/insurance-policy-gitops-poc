FROM ibmcom/websphere-liberty:latest

# Remove any stale app to prevent "two applications with same name" error
RUN rm -f /opt/ibm/wlp/usr/servers/defaultServer/dropins/insurance-health-component* || true

# Copy server config and WAR
COPY server.xml /config/
COPY build/libs/insurance-health-component.war /config/dropins/

EXPOSE 9080 9443