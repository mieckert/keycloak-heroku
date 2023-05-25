package net.brickverse.test;

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;

public class EpicGamesIdentityProviderFactory extends AbstractIdentityProviderFactory<EpicGamesIdentityProvider> implements SocialIdentityProviderFactory<EpicGamesIdentityProvider> {
    @Override
    public String getName() {
        return "Epic Games";
    }

    @Override
    public EpicGamesIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new EpicGamesIdentityProvider(session, new EpicGamesIdentityProviderConfig(model));
    }

    @Override
    public IdentityProviderModel createConfig() {
        return new OAuth2IdentityProviderConfig();
    }

    @Override
    public String getId() {
        return "EpicGames";
    }
}
