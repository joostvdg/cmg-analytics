# syntax=docker/dockerfile:experimental
#######################################
FROM adoptopenjdk:11.0.5_10-jre-openj9-0.17.0
# This is a fake SENTRY DSN, replace ENV with actual Value
ENV SENTRY_DSN=https://public:private@host:port/1
ENV PORT=8080
ENV JDBC_DATABASE_URL="jdbc:postgresql://localhost:5432"
ENV JDBC_DATABASE_USERNAME="docker"
ENV JDBC_DATABASE_PASSWORD="docker"
COPY target/cmg-analytics-*.jar ./app.jar
### Have to use ENTRYPOINT exec to avoid issues with parameter interpolation and avoid an entrypoint.sh script
# see: https://joostvdg.github.io/blogs/docker-graceful-shutdown/
#ENTRYPOINT
### Heroku expect a CMD command, so we have to use it
# https://stackoverflow.com/questions/55913408/no-command-specified-for-process-type-web-on-heroku
EXPOSE 8080
COPY docker-run.sh /run.sh
RUN chmod +x /run.sh
CMD ["/run.sh"]
#######################################
