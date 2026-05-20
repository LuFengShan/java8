package com.java8.oauth2;

public class OAuth2SimpleExample {

    public static void main(String[] args) {
        System.out.println("=== OAuth 2.0 四种授权模式简介 ===\n");

        System.out.println("1. 授权码模式 (Authorization Code)");
        System.out.println("   - 最安全、最常用的模式");
        System.out.println("   - 适用于有后端的Web应用");
        System.out.println("   - 流程: 授权码 -> 访问令牌\n");

        System.out.println("2. 简化模式 (Implicit)");
        System.out.println("   - 直接返回访问令牌，不经过授权码");
        System.out.println("   - 适用于纯前端应用（如SPA）");
        System.out.println("   - 安全性较低\n");

        System.out.println("3. 密码模式 (Resource Owner Password Credentials)");
        System.out.println("   - 用户直接把用户名密码给应用");
        System.out.println("   - 适用于高度信任的应用");
        System.out.println("   - 不推荐使用\n");

        System.out.println("4. 客户端模式 (Client Credentials)");
        System.out.println("   - 应用直接以自己的名义访问");
        System.out.println("   - 适用于机器之间的通信");
        System.out.println("   - 不涉及用户\n");

        System.out.println("详细演示请运行 OAuth2Demo 类");
    }
}
