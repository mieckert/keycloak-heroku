@echo off

SET VERSION=%~1
echo "VERSION is %VERSION%"
COPY  "C:\Users\jeff\keycloak-services-social-weixin\target\keycloak-services-social-weixin-%VERSION%.jar"^
    C:\Users\jeff\keycloak-heroku\idps\wechat-mobile\keycloak-services-social-weixin.jar