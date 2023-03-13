package com.yfwj.justauth.social.common;

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

/**
 * @author yanfeiwuji
 * @date 2021/1/12 7:53 下午
 */
public class JustIdentityProviderConfig extends OAuth2IdentityProviderConfig {

  private static final String AGENT_ID_KEY = "weworkAgentId";
  private static final String ALIPAY_PUBLIC_KEY = "alipayPublicKey";
  private static final String CODING_GROUP_NAME = "codingGroupName";
  private static final String DINGTALK_AGENT_ID_KEY = "dingtalkAgentId";

  private JustAuthKey justAuthKey;

  public JustIdentityProviderConfig(IdentityProviderModel model, JustAuthKey justAuthKey) {
    super(model);
    this.justAuthKey = justAuthKey;
  }

  public JustIdentityProviderConfig(JustAuthKey justAuthKey) {
    this.justAuthKey = justAuthKey;
  }



  public JustAuthKey getJustAuthKey() {
    return this.justAuthKey;
  }

  public String getAgentId() {
    return getConfig().get(AGENT_ID_KEY);
  }

  public void setAgentId(String agentId) {
    getConfig().put(AGENT_ID_KEY, agentId);
  }

  public String getAlipayPublicKey() {
    return getConfig().get(ALIPAY_PUBLIC_KEY);
  }

  public void setAlipayPublicKey(String alipayPublicKey) {

    getConfig().put(ALIPAY_PUBLIC_KEY, alipayPublicKey);
  }

  public String getCodingGroupName() {
    return getConfig().get(CODING_GROUP_NAME);
  }

  public void setCodingGroupName(String codingGroupName) {
    getConfig().put(CODING_GROUP_NAME, codingGroupName);

  }


}
