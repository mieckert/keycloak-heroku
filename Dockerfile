FROM openjdk:11.0.5-jdk as openjdk
FROM jboss/keycloak:latest

COPY docker-entrypoint.sh /opt/jboss/tools
COPY --from=openjdk /usr/local/openjdk-11/conf/security/java.security /etc/alternatives/jre/conf/security/

COPY idps/wechat-mobile/keycloak-services-social-weixin.jar \
    /opt/jboss/keycloak/providers/

COPY idps/wechat-mobile/templates/realm-identity-provider-weixin-ext.html \
    /opt/jboss/keycloak/themes/base/admin/resources/partials

COPY idps/wechat-mobile/templates/realm-identity-provider-weixin.html \
    /opt/jboss/keycloak/themes/base/admin/resources/partials

RUN sed -ie 's#<dependencies>#<dependencies><module name="com.google.code.gson" services="import"/>#' /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/module.xml

COPY idps/wecom/keycloak-services-social-wechat-work.jar \
    /opt/jboss/keycloak/providers/

COPY idps/wecom/templates/realm-identity-provider-wechat-work.html \
    /opt/jboss/keycloak/themes/base/admin/resources/partials
COPY idps/wecom/templates/realm-identity-provider-wechat-work-ext.html \
    /opt/jboss/keycloak/themes/base/admin/resources/partials

# add `<module name="org.infinispan" services="import"/>` to dependencies
RUN sed -ie 's#<dependencies>#<dependencies><module name="org.infinispan" services="import"/>#' /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/module.xml

COPY  target/keycloak-justauth-12.0.1-jar-with-dependencies.jar /opt/jboss/keycloak/providers/
COPY  target/keycloak-justauth-12.0.1-jar-with-dependencies.jar /opt/jboss/keycloak/standalone/deployments

COPY  temp/* /opt/jboss/keycloak/themes/base/admin/resources/partials/
COPY  ui/font_iconfont /opt/jboss/keycloak/themes/keycloak/common/resources/lib/font_iconfont
COPY  ui/theme.properties /opt/jboss/keycloak/themes/keycloak/login/

#
#COPY idps/sms/keycloak-sms-authenticator.jar \
#    /opt/jboss/keycloak/providers/
#COPY idps/sms/templates/sms-validation.ftl \
#    /opt/jboss/keycloak/themes/base/login/
#COPY idps/sms/templates/sms-validation-error.ftl \
#    /opt/jboss/keycloak/themes/base/login/

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]
CMD ["-b", "0.0.0.0"]

