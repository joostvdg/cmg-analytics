export DIND_IP=$(docker inspect dockerdind --format='{{.NetworkSettings.Networks.bridge.IPAddress}}')
echo "${DIND_IP}"
