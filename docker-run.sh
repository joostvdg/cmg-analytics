#!/usr/bin/env bash

echo "HELLO"
EXTRA_CONFIG=""
echo "PORT=${PORT}"
if [ "${DATABASE_URL}" ]; then
echo "HELLO=$DATABASE_URL"
    URL=$DATABASE_URL
    INDEX_AT=`expr index "$URL" @`
    LENGTH_PREFIX=11
    LENGTH_AT=$[ INDEX_AT - LENGTH_PREFIX ]
    LENGTH_AT=$[LENGTH_AT -1 ]
    USR_PASS=${URL:11:$LENGTH_AT}
    INDEX_COLON=`expr index "$USR_PASS" :`
    USR=${USR_PASS:0:$INDEX_COLON-1}
    PSS=${USR_PASS:$INDEX_COLON}
    URL_REMAINDER=${DATABASE_URL:$LENGTH_AT + $LENGTH_PREFIX + 1}
    export JDBC_DATABASE_URL="jdbc:postgresql://$URL_REMAINDER"
    echo "JDBC_DATABASE_URL=$JDBC_DATABASE_URL"
    export JDBC_DATABASE_USERNAME="$USR"
    export JDBC_DATABASE_PASSWORD="$PSS"
fi
java -Xms256M -Xmx480M -Djava.security.egd=file:/dev/./urandom \
  -Dmicronaut.server.port=${PORT} \
  -Dmicronaut.server.host="0.0.0.0" \
  -Djooq.datasources.default.url=${JDBC_DATABASE_URL} \
  -Djooq.datasources.default.username=${JDBC_DATABASE_USERNAME} \
  -Djooq.datasources.default.password=${JDBC_DATABASE_PASSWORD} \
  -Ddatasources.default.url=${JDBC_DATABASE_URL} \
  -Ddatasources.default.username=${JDBC_DATABASE_USERNAME} \
  -Ddatasources.default.password=${JDBC_DATABASE_PASSWORD} \
  -Dcmg.analytics.user=${CMG_USER} \
  -Dcmg.analytics.password=${CMG_PASSWORD} \
  -XX:+UnlockExperimentalVMOptions \
  -XX:+UseCGroupMemoryLimitForHeap \
  -Dcom.sun.management.jmxremote \
  -noverify \
  -jar app.jar
