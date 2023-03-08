package com.yfwj.justauth.social;

import com.yfwj.justauth.social.common.JustAuthKey;
import com.yfwj.justauth.social.common.JustIdentityProvider;
import com.yfwj.justauth.social.common.JustIdentityProviderConfig;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;

/**
 * @author yanfeiwuji
 * @date 2021/1/10 5:48 下午
 */

public class AliyunIdentityProviderFactory extends
  AbstractIdentityProviderFactory<JustIdentityProvider>
  implements SocialIdentityProviderFactory<JustIdentityProvider> {

  public static final JustAuthKey JUST_AUTH_KEY = JustAuthKey.  ALIYUN;

  @Override
  public String getName() {
    return JUST_AUTH_KEY.getName();
  }

  @Override
  public JustIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
    return new JustIdentityProvider(session, new JustIdentityProviderConfig(model,JUST_AUTH_KEY));
  }

  @Override
  public OAuth2IdentityProviderConfig createConfig() {
    return new OAuth2IdentityProviderConfig();
  }

  @Override
  public String getId() {
    return JUST_AUTH_KEY.getId();
  }
}
