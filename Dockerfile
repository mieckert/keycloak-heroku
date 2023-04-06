FROM quay.io/keycloak/keycloak:latest

COPY idps/wechat-mobile/keycloak-services-social-weixin.jar \
    /opt/keycloak/providers/
COPY idps/wechat-mobile/templates/realm-identity-provider-weixin-ext.html \
    /opt/keycloak/themes/base/admin/resources/partials
COPY idps/wechat-mobile/templates/realm-identity-provider-weixin.html \
    /opt/keycloak/themes/base/admin/resources/partials

COPY idps/wecom/keycloak-services-social-wechat-work.jar \
    /opt/keycloak/providers/
COPY idps/wecom/templates/realm-identity-provider-wechat-work.html \
    /opt/keycloak/themes/base/admin/resources/partials
COPY idps/wecom/templates/realm-identity-provider-wechat-work-ext.html \
    /opt/keycloak/themes/base/admin/resources/partials


#COPY  temp/* /opt/keycloak/themes/base/admin/resources/partials
COPY  ui/font_iconfont /opt/keycloak/themes/keycloak/common/resources/lib/font_iconfont
COPY  ui/theme.properties /opt/keycloak/themes/keycloak/login/
COPY  target/keycloak-justauth-12.0.1-jar-with-dependencies.jar /opt/keycloak/providers/

COPY  target/keycloak-justauth-12.0.1-jar-with-dependencies.jar /opt/keycloak/deployments/

ENV KC_HOSTNAME_STRICT=false
ENV KC_HOSTNAME_STRICT_HTTPS=false
ENV KC_HTTP_ENABLED=true

CMD ["start-dev", "--hostname-strict=false"]

