FROM openjdk:8-jdk-alpine
MAINTAINER aerwin3@gmail.com
COPY /images/target/images-1.0.0-SNAPSHOT.jar images-1.0.0-SNAPSHOT.jar
COPY gcp-credentials.json /opt/gcp_creds.json
ENTRYPOINT ["java","-jar","/images-1.0.0-SNAPSHOT.jar"]