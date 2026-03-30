FROM open-liberty:full-java11-openj9

COPY app/build/libs/*.ear /config/apps/
COPY app/src/main/liberty/config/server.xml /config/

EXPOSE 9080 9443
