FROM ibmcom/websphere-liberty:latest

# Remove any pre-existing copy of the app to avoid
# "It is not possible to start two applications called ..." errors on restart.
RUN rm -f /opt/ibm/wlp/usr/servers/defaultServer/dropins/insurance-health-component* || true

# Copy the H2 driver into Liberty's shared resources directory.
# server.xml references it via <library id="H2Lib"> so Liberty can create
# the container-managed DataSource. The jar must NOT be inside the WAR.
# build/h2/h2.jar is produced by the Gradle h2Copy task before docker build.
COPY build/h2/h2.jar /opt/ibm/wlp/usr/shared/resources/h2.jar

# Copy server config and application
COPY server.xml /config/
COPY build/libs/insurance-health-component.war /config/dropins/

EXPOSE 9080 9443