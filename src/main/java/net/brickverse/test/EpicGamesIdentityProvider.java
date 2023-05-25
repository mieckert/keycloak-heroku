package net.brickverse.test;

import com.alibaba.fastjson.JSON;
import org.keycloak.broker.provider.AbstractIdentityProvider;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;

import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class EpicGamesIdentityProvider extends AbstractIdentityProvider<EpicGamesIdentityProviderConfig> implements SocialIdentityProvider<EpicGamesIdentityProviderConfig> {
    private static final Logger logger = Logger.getLogger(String.valueOf(EpicGamesIdentityProvider.class));

    public EpicGamesIdentityProvider(KeycloakSession session, EpicGamesIdentityProviderConfig config) {
        super(session, config);

        logger.info("EpicGamesIdentityProvider config: " + JSON.toJSONString(config));
    }

    @Override
    public Response retrieveToken(KeycloakSession session, FederatedIdentityModel identity) {
        return null;
    }
}
