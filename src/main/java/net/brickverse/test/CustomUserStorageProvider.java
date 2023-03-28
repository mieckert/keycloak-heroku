package net.brickverse.test;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

import java.util.*;

public class CustomUserStorageProvider implements UserStorageProvider, CredentialInputValidator, UserLookupProvider {
    private static final Logger logger = Logger.getLogger(CustomUserStorageProvider.class);

    private KeycloakSession session;
    private ComponentModel model;
    protected Properties properties;

    /**
     * map of loaded users in this transaction
     */
    protected Map<String, UserModel> loadedUsers = new HashMap<>();

    public CustomUserStorageProvider(KeycloakSession session, ComponentModel model, Properties properties) {
        this.session = session;
        this.model = model;
        this.properties = new Properties();

        this.properties.put("test-user", "test-password");
    }

    @Override
    public void close() {
        // 关闭CustomUserStorageProvider
    }

    //=============CredentialInputValidator 实现 start
    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    /**
     * 运行时将调用该方法，以确定是否为用户配置了特定的凭据类型。此方法检查是否已为用户设置了密码
     */
    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        String password = properties.getProperty(userModel.getUsername());
        return credentialType.equals(PasswordCredentialModel.TYPE) && password != null;
    }

    /**
     * 方法负责验证密码。
     * 该CredentialInput参数实际上只是所有凭证类型的抽象接口。
     * 我们确保我们支持凭证类型，并且它也是的实例UserredentialModel。
     * 当用户通过登录页面登录时，密码输入的纯文本将放入的实例UserCredentialModel。
     * 该isValid()方法根据存储在属性文件中的纯文本密码检查此值。返回值true表示密码有效。
     */
    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput input) {
        logger.info("CredentialInputValidator.isValid");
        if (!supportsCredentialType(input.getType())) return false;
        String password = properties.getProperty(userModel.getUsername());
        if (password == null) return false;
        return password.equals(input.getChallengeResponse());
    }
    //=============CredentialInputValidator实现 end


    //=============UserLookupProvider 实现 start

    /**
     * 通过用户名查询用户
     */
    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        UserModel adapter = loadedUsers.get(username);
        if (adapter == null) {
            String password = properties.getProperty(username);
            if (password != null) {
                adapter = createAdapter(realm, username);
                loadedUsers.put(username, adapter);
            }
        }
        return adapter;
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        return this.getUserByUsername(username, realm);
    }

    /**
     * 创建用户模型
     */
    protected UserModel createAdapter(RealmModel realm, String username) {
        return new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() {
                return username;
            }
        };
    }

    /**
     * 通过用户ID查询用户
     */
    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return getUserByUsername(username, realm);
    }

    /**
     * 通过邮箱查查询用户
     */
    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return null;
    }
    //=============UserLookupProvider 实现 end
}
