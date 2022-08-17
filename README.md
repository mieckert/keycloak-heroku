# keycloak-heroku

---

> 一键部署 Keycloak 到 Heroku 平台。 Deploy Keycloak to Heroku by just one click.

## 中文版说明


[![部署到 Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

Keycloak 是一款优秀的开源身份与访问管理系统，直接部署到 Heroku 会存在一个小问题，因此需要做一些小调整。本仓库基于上游的调整，升级了 Keycloak 的版本，并适配了 Heroku 的免费 
Dyno，从而不需要付费，免费拥有一个 Keycloak 实例。 

## 更多说明：

- [FreeArch: 一键拥有你自己的身份认证平台 Keycloak，完全免费！
  ](https://zhuanlan.zhihu.com/p/554534245)

## English README



[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)



This repository deploys the [Keycloak](https://www.keycloak.org) Identity and Access Management Solution 
to Heroku.  It is based of Keycloak's official docker image with some slight modifications to use the
Heroku variable for `PORT` and `DATABASE_URL` properly.

The deployment will be made with a single Free dyno (although it won't run very well in smaller dynos
due to Java's memory hunger, it can be used as testing purpose without issues) with a free Postgres database attached.
