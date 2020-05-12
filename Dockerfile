FROM openjdk:11.0.5-jdk as openjdk
FROM jboss/keycloak:latest

COPY docker-entrypoint.sh /opt/jboss/tools
COPY --from=openjdk /usr/local/openjdk-11/conf/security/java.security /etc/alternatives/jre/conf/security/

COPY idps/wechat-qr-code/7.0.0/keycloak-social-wechat-v7.0.0.jar /opt/jboss/keycloak/providers/

COPY idps/wechat-qr-code/7.0.0/templates/realm-identity-provider-wechat.html /opt/jboss/keycloak/themes/base/admin/resources/partials

COPY idps/wechat-qr-code/7.0.0/templates/realm-identity-provider-wechat-ext.html /opt/jboss/keycloak/themes/base/admin/resources/partials

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]
CMD ["-b", "0.0.0.0"]

