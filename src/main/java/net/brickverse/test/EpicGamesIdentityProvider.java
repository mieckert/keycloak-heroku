package net.brickverse.test;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.var;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.keycloak.broker.provider.AbstractIdentityProvider;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;

import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.logging.Logger;

public class EpicGamesIdentityProvider extends AbstractIdentityProvider<EpicGamesIdentityProviderConfig> implements SocialIdentityProvider<EpicGamesIdentityProviderConfig> {
    private static final Logger logger = Logger.getLogger(String.valueOf(EpicGamesIdentityProvider.class));
    private EpicGamesIdentityProviderConfig config;

    public EpicGamesIdentityProvider(KeycloakSession session, EpicGamesIdentityProviderConfig config) {
        super(session, config);
        this.config = config;
    }

    @SneakyThrows
    @Override
    public Response retrieveToken(KeycloakSession session, FederatedIdentityModel identity) {
        logger.info("EpicGamesIdentityProvider config: " + JSON.toJSONString(config));
        var credentials = config.getClientId() + ":" + config.getClientSecret();
        var base64BasicAuth = Base64.getEncoder().encodeToString(credentials.getBytes());
        logger.info("base64BasicAuth: " + base64BasicAuth);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=authorization_code&deployment_id=73ff9697627941b3a4d18dd7d70a1a1d&scope=basic_profile&code=95fe72821ac94e5f9c48d5dda9b42e5c");
        Request request = new Request.Builder()
                .url("https://api.epicgames.dev/epic/oauth/v1/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + base64BasicAuth)
                .build();
        var response = client.newCall(request).execute();
        return ResponseConverter.convert(response);
    }
}
