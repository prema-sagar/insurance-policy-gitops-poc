FROM ibmcom/websphere-liberty:latest

# Remove any stale app to prevent "two applications with same name" error
RUN rm -f /opt/ibm/wlp/usr/servers/defaultServer/dropins/insurance-health-component* || true

# Download Apache Derby into Liberty's shared resources directory.
# Derby is NOT bundled in the Liberty image, so we fetch it here.
# This runs at image build time (GitHub Actions / local docker build).
# Using Maven Central — the same version Liberty's JPA stack is tested against.
RUN mkdir -p /opt/ibm/wlp/usr/shared/resources/derby && \
    curl -fsSL \
      https://repo1.maven.org/maven2/org/apache/derby/derby/10.14.2.0/derby-10.14.2.0.jar \
      -o /opt/ibm/wlp/usr/shared/resources/derby/derby.jar && \
    curl -fsSL \
      https://repo1.maven.org/maven2/org/apache/derby/derbytools/10.14.2.0/derbytools-10.14.2.0.jar \
      -o /opt/ibm/wlp/usr/shared/resources/derby/derbytools.jar

# Copy server config and WAR
COPY server.xml /config/
COPY build/libs/insurance-health-component.war /config/dropins/

EXPOSE 9080 9443