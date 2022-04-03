#!/usr/bin/env bash
JWT_SECRET=$(openssl rand -base64 32)
echo "JWT_SECRET=${JWT_SECRET}"
./mvn-command.sh "compile exec:exec -Dmicronaut.security.token.jwt.signatures.secret.generator.secret=${JWT_SECRET}"
