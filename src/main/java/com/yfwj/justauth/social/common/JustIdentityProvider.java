package com.yfwj.justauth.social.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.ErrorPage;
import org.keycloak.sessions.AuthenticationSessionModel;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.lang.reflect.Constructor;

import javax.ws.rs.core.UriInfo;

public class JustIdentityProvider extends AbstractOAuth2IdentityProvider<JustIdentityProviderConfig> implements SocialIdentityProvider<JustIdentityProviderConfig> {

  public final String DEFAULT_SCOPES = "default";
  //OAuth2IdentityProviderConfig
  public final AuthConfig AUTH_CONFIG;
  public final Class<? extends AuthDefaultRequest> tClass;


  public JustIdentityProvider(KeycloakSession session, JustIdentityProviderConfig config) {
    super(session, config);

    logger.info("JustIdentityProvider config: " + JSON.toJSONString(config));

    JustAuthKey justAuthKey = config.getJustAuthKey();
    this.AUTH_CONFIG = JustAuthKey.getAuthConfig(config);
    logger.info("JustIdentityProvider authConfig: " + JSON.toJSONString(AUTH_CONFIG));
    this.tClass = justAuthKey.getTClass();
  }

  @Override
  protected UriBuilder createAuthorizationUrl(AuthenticationRequest request) {
    String redirectUri = request.getRedirectUri();
    AuthRequest authRequest = getAuthRequest(AUTH_CONFIG, redirectUri);
    String uri = authRequest.authorize(request.getState().getEncoded());
    return UriBuilder.fromUri(uri);
  }

  private AuthRequest getAuthRequest(AuthConfig authConfig, String redirectUri) {
    logger.info("Getting auth request with authConfig: " + JSON.toJSONString(authConfig) + " and redirectUri: " + redirectUri + " and tClass: " + tClass);

    AuthRequest authRequest = null;
    authConfig.setRedirectUri(redirectUri);
    try {
      Constructor<? extends AuthDefaultRequest> constructor = tClass.getConstructor(AuthConfig.class);
      authRequest = constructor.newInstance(authConfig);
      logger.info("authRequest: " + JSON.toJSONString(authRequest) + " with authConfig: " + JSON.toJSONString(authConfig));
    } catch (Exception e) {
      // can't
      logger.error("getting auth request met error: " + e.getMessage());
      e.printStackTrace();
    }

    return authRequest;
  }

  @Override
  protected String getDefaultScopes() {
    return DEFAULT_SCOPES;
  }

  @Override
  public Object callback(RealmModel realm, AuthenticationCallback callback, EventBuilder event) {
    return new Endpoint(callback, realm, event);
  }


  protected class Endpoint {
    protected AuthenticationCallback callback;
    protected RealmModel realm;
    protected EventBuilder event;
    @Context
    protected KeycloakSession session;
    @Context
    protected ClientConnection clientConnection;
    @Context
    protected HttpHeaders headers;
    @Context
    protected UriInfo uriInfo;


    public Endpoint(AuthenticationCallback callback, RealmModel realm, EventBuilder event) {
      this.callback = callback;
      this.realm = realm;
      this.event = event;
    }

    @GET
    public Response authResponse(@QueryParam("state") String state,
                                 @QueryParam("code") String authorizationCode,
                                 @QueryParam("error") String error) {
      logger.info("authResponse with state: " + state + " and authorizationCode: " + authorizationCode + " and error: " + error);
      AuthCallback authCallback = AuthCallback.builder().code(authorizationCode).state(state).build();

      String redirectUri = uriInfo.getAbsolutePath().toString();
      AuthRequest authRequest = getAuthRequest(AUTH_CONFIG, redirectUri);
      try {
        AuthResponse<AuthUser> response = authRequest.login(authCallback);

        if (response.ok()) {
          AuthUser authUser = response.getData();
          JustIdentityProviderConfig config = JustIdentityProvider.this.getConfig();
          BrokeredIdentityContext federatedIdentity = new BrokeredIdentityContext(authUser.getUuid());
          authUser.getRawUserInfo().forEach((k, v) -> {
            String value = (v instanceof String) ? v.toString() : JSONObject.toJSONString(v);
            // v  不能过长
            federatedIdentity.setUserAttribute(config.getAlias() + "-" + k, value);
          });

          if (getConfig().isStoreToken()) {
            // make sure that token wasn't already set by getFederatedIdentity();
            // want to be able to allow provider to set the token itself.
            if (federatedIdentity.getToken() == null) {
              federatedIdentity.setToken(authUser.getToken().getAccessToken());
            }
          }

          federatedIdentity.setUsername(authUser.getUuid());
          federatedIdentity.setBrokerUserId(authUser.getUuid());
          federatedIdentity.setIdpConfig(config);
          federatedIdentity.setIdp(JustIdentityProvider.this);
//          federatedIdentity.setCode(state);

          return this.callback.authenticated(federatedIdentity);
        } else {
          return this.errorIdentityProviderLogin("identityProviderUnexpectedErrorMessage");
        }
      }catch(Exception e){
        logger.error("authResponse met error: " + e.getMessage());
        e.printStackTrace();

        return this.errorIdentityProviderLogin("identityProviderUnexpectedErrorMessage");
      }
    }

    private Response errorIdentityProviderLogin(String message) {
      this.event.event(EventType.LOGIN);
      this.event.error("identity_provider_login_failure");
      return ErrorPage.error(this.session, null, Response.Status.BAD_GATEWAY, message);
    }
  }
}
