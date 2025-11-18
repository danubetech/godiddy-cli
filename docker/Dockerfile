# Dockerfile for godiddy/godiddy-cli

FROM maven:3-eclipse-temurin-21-alpine AS build
LABEL org.opencontainers.image.authors="markus@danubetech.com"

# install dependencies

ADD ./settings.xml /root/.m2/

RUN apk add patch

# build godiddy-cli

ADD pom.xml /opt/godiddy-cli/
RUN --mount=type=secret,id=DANUBETECH_MAVEN_INTERNAL_USERNAME,env=DANUBETECH_MAVEN_INTERNAL_USERNAME --mount=type=secret,id=DANUBETECH_MAVEN_INTERNAL_PASSWORD,env=DANUBETECH_MAVEN_INTERNAL_PASSWORD \
    cd /opt/godiddy-cli/ && mvn org.apache.maven.plugins:maven-dependency-plugin:3.8.1:go-offline

ADD . /opt/godiddy-cli/
RUN cd /opt/godiddy-cli/ && mvn clean package -DskipTests

# build image

FROM eclipse-temurin:21-jre-alpine
LABEL org.opencontainers.image.authors="markus@danubetech.com"

RUN apk add libsodium

COPY --from=build /opt/godiddy-cli/target/*.jar /opt/godiddy-cli/
RUN ln -s /opt/godiddy-cli/godiddy-cli-*-jar-with-dependencies.jar /opt/godiddy-cli/godiddy-cli.jar

# variables

ENV godiddy.cli.dummy=dummy

# done

WORKDIR /opt/godiddy-cli/
VOLUME godiddy-cli-storage:/opt/godiddy-cli/godiddy-cli-storage/
ENTRYPOINT [ "java", "-jar", "/opt/godiddy-cli/godiddy-cli.jar" ]
