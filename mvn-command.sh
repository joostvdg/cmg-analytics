#!/usr/bin/env bash
COMMAND=${1}
echo "Maven Command: ${COMMAND}"

if [[ "$OSTYPE" == "linux-gnu" ]]; then
    DB_IP=$(docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' cmg-analytics_db_1)
    PORT=5432
elif [[ "$OSTYPE" == "darwin"* ]]; then
    DB_IP="localhost"
    PORT=8432
else
    echo "I don't know this OS: ${OSTYPE} exiting"
    exit 1
fi

echo "OSTYPE=${OSTYPE}"
echo "DB_IP=$DB_IP"
echo "PORT=$PORT"
JDBC_DATABASE_URL="jdbc:postgresql://${DB_IP}:${PORT}/cmg"
echo "JDBC_DATABASE_URL=${JDBC_DATABASE_URL}"
mvn clean -e --show-version ${COMMAND} \
  -Ddb.url=${JDBC_DATABASE_URL} \
  -Djooq.datasources.default.url=${JDBC_DATABASE_URL} \
  -Ddatasources.default.url=${JDBC_DATABASE_URL}
