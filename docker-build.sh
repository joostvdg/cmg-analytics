#!/usr/bin/env bash
./mvn-build.sh
docker image build -t cmg-analytics:latest .
