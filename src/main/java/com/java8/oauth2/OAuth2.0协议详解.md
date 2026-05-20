# OAuth 2.0 协议详解 - 通俗易懂版

## 一、什么是 OAuth 2.0？

### 1.1 生活中的类比

想象一下这个场景：
- 你是**用户**（Resource Owner）
- 你有一个**小区门禁卡**（你的账号密码）
- 你的朋友（第三方应用）想进你家取点东西

如果直接把门禁卡给朋友，风险太大了！朋友可能会拿着卡到处乱用。

OAuth 2.0 就是解决这个问题的：
- 你不用给朋友门禁卡
- 你去物业（授权服务器）开一张**临时通行证**（访问令牌）
- 朋友拿着临时通行证只能进你家取东西，不能干别的

### 1.2 正式定义

OAuth 2.0 是一个**授权框架**，让第三方应用可以在**不获取用户账号密码**的情况下，获得访问用户资源的权限。

---

## 二、OAuth 2.0 的四个核心角色

| 角色 | 英文名 | 说明 | 生活中的例子 |
|------|--------|------|-------------|
| 资源所有者 | Resource Owner | 拥有资源的人 | 你（房子的主人） |
| 资源服务器 | Resource Server | 存放资源的地方 | 你家（存放你的东西） |
| 客户端 | Client | 想要访问资源的第三方应用 | 你的朋友（想进你家取东西） |
| 授权服务器 | Authorization Server | 负责颁发令牌的机构 | 小区物业（负责开临时通行证） |

---

## 三、四种授权模式

### 3.1 授权码模式（Authorization Code）- 最常用！

#### 适用场景
- 有后端服务器的 Web 应用
- 手机 App
- 最安全的模式

#### 完整流程（用取快递的例子）

```
1. 用户访问应用
   用户："我想让这个应用帮我取快递"

2. 应用重定向到授权服务器
   应用："用户，去物业那里授权一下"
   用户被重定向到：https://auth-server.com/authorize?client_id=xxx&redirect_uri=xxx

3. 用户登录并授权
   用户："物业，我同意让这个应用帮我取快递"

4. 授权服务器返回授权码
   物业："好的，这是授权码，给应用"
   重定向回：https://app.com/callback?code=授权码

5. 应用用授权码换取访问令牌
   应用："物业，这是授权码，给我通行证"
   物业验证后返回：access_token=xxx

6. 应用用访问令牌访问资源
   应用："快递柜，这是通行证，把快递给我"
   快递柜验证令牌后取出快递
```

#### 代码示例

参见 [OAuth2Demo.java](file:///Users/sunguangxu/github_code/java8/src/main/java/com/java8/oauth2/OAuth2Demo.java)

---

### 3.2 简化模式（Implicit）

#### 适用场景
- 纯前端应用（SPA - 单页应用）
- 没有后端服务器
- 安全性要求不高

#### 流程特点
- 跳过第5步（授权码换令牌）
- 直接在第4步返回访问令牌
- 令牌在 URL 哈希中，不经过后端

#### 缺点
- 令牌容易泄露
- 没有刷新令牌机制

---

### 3.3 密码模式（Resource Owner Password Credentials）

#### 适用场景
- 用户高度信任的应用（如官方 App）
- 其他模式无法使用时

#### 流程
```
1. 用户直接把用户名密码给应用
2. 应用用用户名密码换取令牌
```

#### 缺点
- 用户把密码给了应用
- 应用可以存储密码
- **不推荐使用！**

---

### 3.4 客户端模式（Client Credentials）

#### 适用场景
- 机器对机器（M2M）通信
- 不涉及用户
- 比如：定时任务、后台服务

#### 流程
```
1. 应用用自己的 client_id 和 client_secret 直接换取令牌
2. 用令牌访问资源
```

---

## 四、重要概念

### 4.1 Access Token（访问令牌）

- 类似于**临时通行证**
- 有过期时间（通常几小时）
- 每次请求资源都要带上
- 格式：通常是 JWT 或随机字符串

### 4.2 Refresh Token（刷新令牌）

- 用于**刷新访问令牌**
- 有效期更长（几天或几周）
- 不需要用户重新登录
- 只在授权码模式下才有

### 4.3 Scope（权限范围）

- 指定令牌可以做什么
- 比如：`read`（只读）、`write`（读写）
- 用户可以只授权部分权限

### 4.4 Client ID / Client Secret

- Client ID：应用的**身份证号**（公开的）
- Client Secret：应用的**密码**（保密的）
- 应用在授权服务器注册时获得

---

## 五、实际例子：用 GitHub 登录

### 场景
你开发了一个网站，想让用户用 GitHub 账号登录。

### 完整流程

```
1. 用户点击"用 GitHub 登录"
   你的网站把用户重定向到：
   https://github.com/login/oauth/authorize?client_id=你的ID&redirect_uri=你的回调地址

2. 用户在 GitHub 登录并授权
   GitHub 问用户："是否允许这个应用访问你的资料？"

3. GitHub 回调你的网站，带上授权码
   重定向到：https://你的网站/callback?code=xxx

4. 你的网站后端用授权码换令牌
   POST https://github.com/login/oauth/access_token
   参数：client_id, client_secret, code, redirect_uri

5. GitHub 返回 access_token
   响应：access_token=gho_xxx

6. 你的网站用 access_token 获取用户信息
   GET https://api.github.com/user
   Header: Authorization: token gho_xxx

7. 登录成功！
```

---

## 六、安全性注意事项

### 6.1 必须使用 HTTPS
- 所有通信都要加密
- 防止令牌被截获

### 6.2 授权码只能用一次
- 用后立即失效
- 防止重放攻击

### 6.3 令牌要安全存储
- 前端：存在 localStorage 或 sessionStorage
- 后端：存在数据库或缓存

### 6.4 设置合理的过期时间
- Access Token：短一些（1-2小时）
- Refresh Token：长一些（7-30天）

### 6.5 使用 PKCE（Proof Key for Code Exchange）
- 针对公开客户端（如手机 App）
- 防止授权码被拦截

---

## 七、总结

| 模式 | 安全性 | 复杂度 | 推荐度 |
|------|--------|--------|--------|
| 授权码模式 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 授权码+PKCE | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 简化模式 | ⭐⭐ | ⭐ | ⭐⭐ |
| 密码模式 | ⭐ | ⭐ | ❌ 不推荐 |
| 客户端模式 | ⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐（机器间通信） |

**最佳实践：**
- Web 应用：用授权码模式
- 手机 App：用授权码+PKCE
- 纯前端：尽量用后端代理，避免简化模式
- 机器通信：用客户端模式

---

## 八、运行示例

```bash
# 编译项目
mvn compile

# 运行 OAuth2Demo
mvn exec:java -Dexec.mainClass="com.java8.oauth2.OAuth2Demo"

# 运行 OAuth2SimpleExample
mvn exec:java -Dexec.mainClass="com.java8.oauth2.OAuth2SimpleExample"
```
