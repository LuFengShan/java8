package com.java8.oauth2;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OAuth2Demo {

    private static final String CLIENT_ID = "my-client-id";
    private static final String CLIENT_SECRET = "my-client-secret";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    private Map<String, String> authorizationCodes = new HashMap<>();
    private Map<String, String> accessTokens = new HashMap<>();
    private Map<String, String> userData = new HashMap<>();

    public OAuth2Demo() {
        userData.put("user123", "张三");
    }

    public String generateAuthorizationCode(String clientId, String userId) {
        String code = UUID.randomUUID().toString();
        authorizationCodes.put(code, userId);
        return code;
    }

    public String exchangeCodeForToken(String code, String clientId, String clientSecret) {
        if (!CLIENT_ID.equals(clientId) || !CLIENT_SECRET.equals(clientSecret)) {
            throw new RuntimeException("客户端凭证无效");
        }

        String userId = authorizationCodes.remove(code);
        if (userId == null) {
            throw new RuntimeException("授权码无效或已过期");
        }

        String accessToken = UUID.randomUUID().toString();
        accessTokens.put(accessToken, userId);
        return accessToken;
    }

    public String getUserInfo(String accessToken) {
        String userId = accessTokens.get(accessToken);
        if (userId == null) {
            throw new RuntimeException("访问令牌无效");
        }
        return userData.get(userId);
    }

    public static void main(String[] args) {
        OAuth2Demo demo = new OAuth2Demo();

        System.out.println("=== OAuth 2.0 授权码模式演示 ===\n");

        System.out.println("步骤1: 用户访问应用，应用请求授权");
        System.out.println("应用重定向用户到授权服务器...\n");

        System.out.println("步骤2: 用户登录并授权");
        System.out.println("用户同意授权后，授权服务器生成授权码\n");

        String authCode = demo.generateAuthorizationCode(CLIENT_ID, "user123");
        System.out.println("授权码: " + authCode + "\n");

        System.out.println("步骤3: 应用使用授权码换取访问令牌");
        String accessToken = demo.exchangeCodeForToken(authCode, CLIENT_ID, CLIENT_SECRET);
        System.out.println("访问令牌: " + accessToken + "\n");

        System.out.println("步骤4: 应用使用访问令牌获取用户信息");
        String userInfo = demo.getUserInfo(accessToken);
        System.out.println("用户信息: " + userInfo);
    }
}
