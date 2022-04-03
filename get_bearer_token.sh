export TOKEN=$(http POST :8082/login 'username=test' 'password=test' --body | jq .access_token)
