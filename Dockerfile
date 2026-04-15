FROM ibmcom/websphere-liberty:latest

# Clean old apps
RUN rm -f /config/apps/* || true

# Install Derby
RUN mkdir -p /opt/ibm/wlp/usr/shared/resources/derby && \
    curl -fsSL https://repo1.maven.org/maven2/org/apache/derby/derby/10.14.2.0/derby-10.14.2.0.jar \
    -o /opt/ibm/wlp/usr/shared/resources/derby/derby.jar && \
    curl -fsSL https://repo1.maven.org/maven2/org/apache/derby/derbytools/10.14.2.0/derbytools-10.14.2.0.jar \
    -o /opt/ibm/wlp/usr/shared/resources/derby/derbytools.jar

# Copy config
COPY server.xml /config/

# ✅ Copy WAR (this is enough)
COPY build/libs/*.war /config/apps/InsuranceApp.war

EXPOSE 9080 9443