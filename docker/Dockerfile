# Dockerfile for danubetech/did-cli

FROM maven:3-eclipse-temurin-21-alpine AS build
LABEL org.opencontainers.image.authors="markus@danubetech.com"

# install dependencies

ADD ./settings.xml /root/.m2/

RUN apk add patch

# build did-cli

ADD pom.xml /opt/did-cli/
RUN --mount=type=secret,id=DANUBETECH_MAVEN_INTERNAL_USERNAME,env=DANUBETECH_MAVEN_INTERNAL_USERNAME --mount=type=secret,id=DANUBETECH_MAVEN_INTERNAL_PASSWORD,env=DANUBETECH_MAVEN_INTERNAL_PASSWORD \
    cd /opt/did-cli/ && mvn org.apache.maven.plugins:maven-dependency-plugin:3.8.1:go-offline

ADD . /opt/did-cli/
RUN cd /opt/did-cli/ && mvn clean package -DskipTests

# build image

FROM eclipse-temurin:21-jre-alpine
LABEL org.opencontainers.image.authors="markus@danubetech.com"

RUN apk add libsodium

COPY --from=build /opt/did-cli/target/*.jar /opt/did-cli/
RUN ln -s /opt/did-cli/did-cli-*-jar-with-dependencies.jar /opt/did-cli/did-cli.jar

# variables

ENV com.danubetech.did.cli.dummy=dummy

# done

WORKDIR /opt/did-cli/
VOLUME did-cli-storage:/opt/did-cli/did-cli-storage/
ENTRYPOINT [ "java", "--enable-native-access=ALL-UNNAMED", "-jar", "/opt/did-cli/did-cli.jar" ]
