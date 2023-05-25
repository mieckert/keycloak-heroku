package net.brickverse.test;

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

public class EpicGamesIdentityProviderConfig extends OAuth2IdentityProviderConfig {
    private static final String CLIENT_ID = "ClientID";
    private static final String CLIENT_SECRET = "ClientSecret";

    private static final String DEPLOYMENT_ID = "DeploymentID";

    public EpicGamesIdentityProviderConfig(IdentityProviderModel model) {
        super(model);
    }

    public EpicGamesIdentityProviderConfig() {

    }

    public String getDeploymentId() {
        return getConfig().get(DEPLOYMENT_ID);
    }

    public void setDeploymentId(String deploymentId) {
        getConfig().put(DEPLOYMENT_ID, deploymentId);
    }

    public String getClientId() {
        return getConfig().get(CLIENT_ID);
    }

    public void setClientId(String clientId) {
        getConfig().put(CLIENT_ID, clientId);
    }

    public String getClientSecret() {
        return getConfig().get(CLIENT_SECRET);
    }

    public void setClientSecret(String clientSecret) {
        getConfig().put(CLIENT_SECRET, clientSecret);
    }
}
