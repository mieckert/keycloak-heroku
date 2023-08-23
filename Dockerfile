ARG KEYCLOAK_VERSION=22.0.1

FROM docker.io/maven:3.8.6-jdk-11 as mvn_builder
COPY . /tmp
RUN cd /tmp && mvn clean install

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION} as builder
COPY --from=mvn_builder /tmp/target/*.jar /opt/keycloak/providers/
COPY --from=mvn_builder /tmp/target/*.jar /opt/keycloak/deployments/

COPY idps/wechat-mobile/keycloak-services-social-weixin.jar \
    /opt/keycloak/providers/

COPY idps/wecom/keycloak-services-social-wechat-work.jar \
    /opt/keycloak/providers/
COPY idps/wecom/templates/realm-identity-provider-wechat-work.html \
    /opt/keycloak/themes/base/admin/resources/partials
COPY idps/wecom/templates/realm-identity-provider-wechat-work-ext.html \
    /opt/keycloak/themes/base/admin/resources/partials


#COPY  temp/* /opt/keycloak/themes/base/admin/resources/partials
#COPY  ui/font_iconfont /opt/keycloak/themes/keycloak/common/resources/lib/font_iconfont
#COPY  ui/theme.properties /opt/keycloak/themes/keycloak/login/

ENV KC_HOSTNAME_STRICT=true
ENV KC_HOSTNAME_STRICT_HTTPS=true
ENV KC_HTTP_ENABLED=false
ENV PROXY_ADDRESS_FORWARDING=true
ENV REDIRECT_SOCKET=proxy-https
ENV FRONTEND_URL=https://keycloak.jiwai.win
ENV KC_HOSTNAME_URL=https://keycloak.jiwai.win

USER 1000

RUN /opt/keycloak/bin/kc.sh build --health-enabled=true

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}
COPY --from=builder /opt/keycloak/ /opt/keycloak/
WORKDIR /opt/keycloak

CMD ["start", "--http-port=$PORT", "--proxy=passthrough", "--db=postgres", "--db-url=$DB_URL", "--db-username=$DB_USERNAME", "--db-password=$DB_PASSWORD", "--features=\"preview,scripts\""]


