FROM openjdk:11.0.5-jdk as openjdk
FROM jboss/keycloak:latest

COPY docker-entrypoint.sh /opt/jboss/tools
COPY --from=openjdk /usr/local/openjdk-11/conf/security/java.security /etc/alternatives/jre/conf/security/

COPY idps/wechat-mobile/0.0.7/keycloak-services-social-weixin-0.0.7.jar \
    /opt/jboss/keycloak/providers/

COPY idps/wechat-mobile/0.0.7/templates/realm-identity-provider-weixin-ext.html \
    /opt/jboss/keycloak/themes/base/admin/resources/partials

COPY idps/wechat-mobile/0.0.7/templates/realm-identity-provider-weixin.html \
    /opt/jboss/keycloak/themes/base/admin/resources/partials

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]
CMD ["-b", "0.0.0.0"]

