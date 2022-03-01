VERSION=$1
echo "VERSION is ${VERSION}"
cp "${HOME}/jeff-tian/keycloak-sms-authenticator/target/keycloak-sms-authenticator-${VERSION}.jar" ~/jeff-tian/keycloak-heroku/idps/sms/keycloak-sms-authenticator.jar