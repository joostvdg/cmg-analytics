TAG=${1}
echo "TAG=${TAG}"
docker build -t cmg-analytics:${TAG} .
