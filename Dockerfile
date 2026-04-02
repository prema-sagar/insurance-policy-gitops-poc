FROM ibmcom/websphere-liberty:latest

# Remove any pre-existing copies of the application to avoid
# "It is not possible to start two applications called ..." errors
RUN rm -f /opt/ibm/wlp/usr/servers/defaultServer/dropins/insurance-health-component* || true

# Copy the H2 driver jar into Liberty's shared resources directory so it can
# be referenced as a <library> in server.xml for the container-managed DataSource.
# The jar is downloaded by Gradle into build/libs via the h2Copy task.
COPY build/h2/h2.jar /opt/ibm/wlp/usr/shared/resources/h2.jar

# Copy server configuration and application WAR
COPY server.xml /config/
COPY build/libs/insurance-health-component.war /config/dropins/

EXPOSE 9080 9443