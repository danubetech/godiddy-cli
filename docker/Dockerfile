# Dockerfile for godiddy/godiddy-cli

FROM maven:3-eclipse-temurin-21-alpine AS build
MAINTAINER Markus Sabadello <markus@danubetech.com>

# build godiddy-cli

ADD . /opt/godiddy-cli
RUN cd /opt/godiddy-cli && mvn clean package -DskipTests

# build image

FROM eclipse-temurin:21-jre-alpine
MAINTAINER Markus Sabadello <markus@danubetech.com>

COPY --from=build /opt/godiddy-cli/target/*.jar /opt/godiddy-cli/

# variables

ENV godiddy.cli.dummy=dummy

# done

ENTRYPOINT [ "java","-jar", "/opt/godiddy-cli/*.jar" ]
