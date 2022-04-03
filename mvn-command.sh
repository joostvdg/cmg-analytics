#!/usr/bin/sh
COMMAND=${1}
echo "Maven Command: ${COMMAND}"

JDBC_DATABASE_URL="jdbc:postgresql://localhost:8432/cmg"
echo "JDBC_DATABASE_URL=${JDBC_DATABASE_URL}"
mvn clean -e --show-version ${COMMAND} \
  -Ddb.url=${JDBC_DATABASE_URL} \
  -Djooq.datasources.default.url=${JDBC_DATABASE_URL} \
  -Ddatasources.default.url=${JDBC_DATABASE_URL}
