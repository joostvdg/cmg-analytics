export DIND_IP=$(docker inspect dockerdind --format='{{.NetworkSettings.Networks.bridge.IPAddress}}')
echo "${DIND_IP}"
docker buildx build . --platform linux/amd64 --tag caladreas/cmg-analytics:0.1.0 --file Dockerfile.multi-stage --build-arg DIND_IP="${DIND_IP}"