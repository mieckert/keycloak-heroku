package com.yfwj.justauth.social.common;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.AuthBaiduScope;
import me.zhyd.oauth.enums.scope.AuthCodingScope;
import me.zhyd.oauth.enums.scope.AuthHuaweiScope;
import me.zhyd.oauth.enums.scope.AuthWeiboScope;
import me.zhyd.oauth.request.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author yanfeiwuji
 * @date 2021/1/12 8:48 下午
 */
public enum JustAuthKey {
  WE_CHAT_MP("wechat_mp","微信公众平台",AuthWeChatMpRequest.class),
  WE_CHAT_OPEN("wechat_open", "微信开放平台", AuthWeChatOpenRequest.class),
  DING_TALK("ding_talk", "钉钉", AuthDingTalkRequest.class),

  BAI_DU("baidu", "百度", AuthBaiduRequest.class, Arrays.asList(
    AuthBaiduScope.BASIC.getScope(),
    AuthBaiduScope.SUPER_MSG.getScope(),
    AuthBaiduScope.NETDISK.getScope()
  )),
  GITEE("gitee", "gitee", AuthGiteeRequest.class),

  WEIBO("weibo", "微博", AuthWeiboRequest.class, Arrays.asList(
    AuthWeiboScope.EMAIL.getScope(),
    AuthWeiboScope.FRIENDSHIPS_GROUPS_READ.getScope(),
    AuthWeiboScope.STATUSES_TO_ME_READ.getScope()
  )),

  CODEING("coding", "coding", AuthCodingRequest.class, Arrays.asList(
    AuthCodingScope.USER.getScope(),
    AuthCodingScope.USER_EMAIL.getScope(),
    AuthCodingScope.USER_PHONE.getScope()
  )),

  OSCHINA("oschina", "开源中国", AuthOschinaRequest.class),
  ALIPAY("alipay", "支付宝", AuthAlipayRequest.class),
  QQ("qq", "QQ", AuthQqRequest.class),
  CSDN("csdn", "CSDN", AuthCsdnRequest.class),
  TAOBAO("taobao", "淘宝", AuthTaobaoRequest.class),
  DOUYIN("douyin", "抖音", AuthDouyinRequest.class),
  MI("mi", "小米", AuthMiRequest.class),
  TOUTIAO("toutiao", "头条", AuthToutiaoRequest.class),
  TEAMBITION("teambition", "Teambition", AuthTeambitionRequest.class),
  PINTEREST("pinterest", "pinterest", AuthPinterestRequest.class),
  RENREN("renren", "人人", AuthRenrenRequest.class),
  HUAWEI("huawei", "华为", AuthHuaweiRequest.class, Arrays.asList(
    AuthHuaweiScope.BASE_PROFILE.getScope(),
    AuthHuaweiScope.MOBILE_NUMBER.getScope(),
    AuthHuaweiScope.ACCOUNTLIST.getScope(),
    AuthHuaweiScope.SCOPE_DRIVE_FILE.getScope(),
    AuthHuaweiScope.SCOPE_DRIVE_APPDATA.getScope()
  )),

  //扫描二维码登陆
  WEWORK("wework", "企业微信", AuthWeChatEnterpriseQrcodeRequest.class),
  // 企业微信客户端内登陆
  WEWORK_WEB("wework_web", "企业微信客户端内登陆", AuthWeChatEnterpriseWebRequest.class),

  KUJIALE("kujiale", "酷家乐", AuthKujialeRequest.class),
  MEITUAN("meituan", "美团", AuthMeituanRequest.class),
  ELEME("eleme", "饿了么", AuthElemeRequest.class),
  ALIYUN("aliyun", "阿里云", AuthAliyunRequest.class),
  XMLY("xmly", "喜马拉雅", AuthXmlyRequest.class),
  AMAZON("amazon", "亚马逊", AuthAmazonRequest.class),
  SLACK("slack","slack",AuthSlackRequest.class),
  LINE("line","line",AuthLineRequest.class),
  OKTA("okta","okta",AuthOktaRequest.class),
  DING_ACCOUNT("ding_account","钉钉账号登陆",AuthDingTalkAccountRequest.class),
  FEISHU("feishu", "飞书", AuthFeishuRequest.class);

  private String id;
  private String name;
  private Class<? extends AuthDefaultRequest> tClass;
  private List<String> scopes;

  private Boolean proxy = false;

  JustAuthKey(String id, String name, Class<? extends AuthDefaultRequest> tClass, List<String> scopes) {
    this(id, name, tClass);
    this.scopes = scopes;
  }

  JustAuthKey(String id, String name, Class<? extends AuthDefaultRequest> tClass, List<String> scopes, boolean proxy) {
    this(id, name, tClass, scopes);
    this.proxy = proxy;
  }

  JustAuthKey(String id, String name, Class<? extends AuthDefaultRequest> tClass) {
    this.id = id;
    this.name = name;
    this.tClass = tClass;
  }

  public AuthConfig.AuthConfigBuilder getAuthConfig() {
    if (scopes != null) {
      return AuthConfig.builder().scopes(this.scopes);
    }
    return AuthConfig.builder();
  }

  public static AuthConfig getAuthConfig(JustIdentityProviderConfig config) {
    JustAuthKey justAuthKey = config.getJustAuthKey();
    String clientId = config.getClientId();
    String clientSecret = config.getClientSecret();
    String agentId = config.getAgentId();
    String alipayPublicKey = config.getAlipayPublicKey();
    String codingGroupName = config.getCodingGroupName();

    AuthConfig.AuthConfigBuilder authConfigBuilder = justAuthKey.getAuthConfig().clientId(clientId).clientSecret(clientSecret);
    switch (justAuthKey) {
      case WEWORK:
        authConfigBuilder.agentId(agentId);
        break;
      case ALIPAY:
        authConfigBuilder.alipayPublicKey(alipayPublicKey);
      case CODEING:
        // coding 的 codingGroupName 为 domainPrefix
        authConfigBuilder.domainPrefix(codingGroupName);
    }

    return authConfigBuilder.build();
  }

  public Class<? extends AuthDefaultRequest> getTClass() {
    return tClass;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }


}
