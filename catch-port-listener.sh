PORT=${1}
lsof -nP -i4TCP:8081 | grep LISTEN
