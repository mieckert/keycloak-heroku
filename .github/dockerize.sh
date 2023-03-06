docker build -f Dockerfile.test -t jefftian/keycloak:"$1" .
docker images
docker run --network host -e CI=true -d -p 127.0.0.1:8080:8080 --name keycloak:"$1"
jefftian/keycloak
docker ps | grep -q keycloak
docker ps -aqf "name=keycloak$"
docker push jefftian/keycloak:"$1"
docker logs $(docker ps -aqf name=keycloak$)
curl localhost:8080 || docker logs $(docker ps -aqf name=keycloak$)
docker kill keycloak || echo "keycloak killed"
docker rm keycloak || echo "keycloak removed"