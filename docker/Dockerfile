# Dockerfile for godiddy/godiddy-cli

FROM maven:3-eclipse-temurin-21-alpine AS build
MAINTAINER Markus Sabadello <markus@danubetech.com>

# install dependencies

ARG DANUBETECH_MAVEN_INTERNAL_USERNAME
ARG DANUBETECH_MAVEN_INTERNAL_PASSWORD
ADD ./settings.xml /root/.m2/

USER root

RUN apk add patch

# build godiddy-cli

ADD . /opt/godiddy-cli
RUN cd /opt/godiddy-cli && mvn clean package -DskipTests

# build image

FROM eclipse-temurin:21-jre-alpine
MAINTAINER Markus Sabadello <markus@danubetech.com>

VOLUME /opt/godiddy-cli/godiddy-cli-data/

COPY --from=build /opt/godiddy-cli/target/*.jar /opt/godiddy-cli/

# variables

ENV godiddy.cli.dummy=dummy

# done

ENTRYPOINT [ "java", "-jar", "/opt/godiddy-cli/cli-0.1-SNAPSHOT-jar-with-dependencies.jar" ]
