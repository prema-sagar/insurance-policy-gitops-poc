FROM ibmcom/websphere-liberty:latest

# Remove any pre-existing copies of the application in the server dropins
# to avoid "It is not possible to start two applications called ..." errors
RUN rm -f /opt/ibm/wlp/usr/servers/defaultServer/dropins/insurance-health-component* || true

# Add H2 driver so the server can provide a container-managed DataSource
# (matches the version used in build.gradle). We put it under the shared
# resources path and reference it from server.xml via a <library> element.
ADD https://repo1.maven.org/maven2/com/h2database/h2/2.1.214/h2-2.1.214.jar /opt/ol/wlp/usr/shared/resources/h2.jar

# Copy server configuration and application WAR into the image
COPY server.xml /config/
COPY build/libs/insurance-health-component.war /config/dropins/

EXPOSE 9080 9443