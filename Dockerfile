# syntax=docker/dockerfile:experimental
#######################################
## Stage 1 : build with maven builder image with native capabilities
FROM maven:3.6-jdk-11-openj9 AS build
WORKDIR /usr/src/app
COPY pom.xml /usr/src/app
RUN --mount=type=cache,target=~/.m2/repository mvn dependency:go-offline
COPY src /usr/src/app/src
RUN --mount=type=cache,target=~/.m2/repository mvn -f /usr/src/app/pom.xml clean package -e --show-version
RUN ls -lath /usr/src/app/
#######################################
## Stage 2 : create the docker final image
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
# This is a fake SENTRY DSN, replace ENV with actual Value
ENV SENTRY_DSN=https://public:private@host:port/1
ENV PORT=8080
COPY --from=build /usr/src/app/target/*.jar ./app.jar
### Have to use ENTRYPOINT exec to avoid issues with parameter interpolation and avoid an entrypoint.sh script
# see: https://joostvdg.github.io/blogs/docker-graceful-shutdown/
#ENTRYPOINT
### Heroku expect a CMD command, so we have to use it
# https://stackoverflow.com/questions/55913408/no-command-specified-for-process-type-web-on-heroku
EXPOSE 8080
CMD java  -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dcom.sun.management.jmxremote -noverify "-Dquarkus.http.port=${PORT}" "-Dquarkus.http.host=0.0.0.0" -jar app.jar
#######################################
