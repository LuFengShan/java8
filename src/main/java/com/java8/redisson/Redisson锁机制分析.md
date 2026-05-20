# Redisson 分布式锁机制深度分析

## 一、代码概述

本文档详细分析 `RedissDemo.java` 中的分布式锁实现，重点讲解 Redisson 锁的核心机制和 Watch Dog 延时功能。

---

## 二、代码逐行分析

### 2.1 配置初始化

```java
Config config = new Config();
config.useSingleServer()
        .setAddress("redis://127.0.0.1:6379")
        .setPassword(null);
config.setLockWatchdogTimeout(30_000L);
redissonClient = Redisson.create(config);
```

**配置说明**：
- `useSingleServer()`：配置单节点 Redis 连接
- `setAddress()`：指定 Redis 地址为 `127.0.0.1:6379`
- `setLockWatchdogTimeout(30_000L)`：设置看门狗超时时间为 **30秒**

### 2.2 获取分布式锁

```java
RLock lock = redissonClient.getLock("sgxLock");
```

**关键知识点**：`getLock()` 返回的实现类

| 锁类型 | 实现类 | 适用场景 |
|-------|--------|---------|
| 普通可重入锁 | `RedissonLock` | 最常用的分布式锁 |
| 公平锁 | `RedissonFairLock` | 需要按顺序获取锁 |
| 红锁 | `RedissonRedLock` | 多节点 Redis 部署 |
| 读写锁 | `RedissonReadWriteLock` | 读多写少场景 |

**默认返回 `RedissonLock`**，它实现了：
- `RLock` 接口（Redisson 扩展）
- `java.util.concurrent.locks.Lock` 接口（JDK 标准）

### 2.3 加锁与业务执行

```java
lock.lock();
System.out.println("get lock success");
Thread.sleep(60_000L);  // 模拟60秒业务操作
```

### 2.4 锁释放

```java
finally {
    if (lock.isHeldByCurrentThread()) {
        lock.unlock();
        System.out.println("释放锁，watch dog stop");
    }
}
```

---

## 三、Redisson 锁的核心机制

### 3.1 锁获取的 Lua 脚本

Redisson 使用 Lua 脚本保证原子性：

```lua
-- 尝试获取锁
if (redis.call('exists', KEYS[1]) == 0) then
    redis.call('hset', KEYS[1], ARGV[2], 1);
    redis.call('pexpire', KEYS[1], ARGV[1]);
    return nil;
end;
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then
    redis.call('hincrby', KEYS[1], ARGV[2], 1);
    redis.call('pexpire', KEYS[1], ARGV[1]);
    return nil;
end;
return redis.call('pttl', KEYS[1]);
```

**执行流程**：
1. **锁不存在** → 创建锁（Hash结构），设置过期时间，返回 `nil`（成功）
2. **锁已存在且当前线程持有** → 增加重入计数，刷新过期时间，返回 `nil`（重入成功）
3. **锁已存在但被其他线程持有** → 返回剩余过期时间（失败）

### 3.2 Watch Dog（看门狗）机制

**核心作用**：自动延长锁的过期时间，防止业务未完成时锁被释放。

#### 工作原理

```
┌─────────────────────────────────────────────────────────────────┐
│                    Redisson Lock Watch Dog                      │
├─────────────────────────────────────────────────────────────────┤
│  1. 获取锁成功                                                  │
│     └─→ 启动定时任务（每10秒执行一次，即30秒的1/3）               │
│                                                                 │
│  2. 定时任务检查                                                │
│     └─→ 当前线程是否仍持有锁？                                    │
│         ├─ 是 → 延长锁的过期时间（重置为30秒）                    │
│         └─ 否 → 停止定时任务                                     │
│                                                                 │
│  3. 锁释放时                                                    │
│     └─→ 自动停止Watch Dog定时任务                               │
└─────────────────────────────────────────────────────────────────┘
```

#### 关键配置参数

```java
config.setLockWatchdogTimeout(30_000L);  // 看门狗超时时间，单位：毫秒
```

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `lockWatchdogTimeout` | 30000ms | 锁的默认过期时间 |
| 定时刷新间隔 | `lockWatchdogTimeout / 3` | 每10秒刷新一次 |

#### 代码场景分析

当前代码中：
```java
lock.lock();                    // 获取锁，默认30秒过期
Thread.sleep(60_000L);         // 业务执行60秒
```

**Watch Dog 执行过程**：
- 第10秒：检测到仍持有锁，延长到30秒
- 第20秒：再次延长到30秒  
- 第30秒：再次延长到30秒
- 第40秒：再次延长到30秒
- 第50秒：再次延长到30秒
- 第60秒：业务完成，释放锁

### 3.3 锁释放的 Lua 脚本

```lua
if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then
    return nil;
end;
local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1);
if (counter > 0) then
    redis.call('pexpire', KEYS[1], ARGV[2]);
    return 0;
else
    redis.call('del', KEYS[1]);
    redis.call('publish', KEYS[2], ARGV[1]);
    return 1;
end;
return nil;
```

**执行流程**：
1. **检查持有者**：非当前线程持有，返回 `nil`
2. **减少计数**：重入计数 > 0，刷新过期时间（支持重入）
3. **计数为0**：删除锁，发布消息通知等待线程

---

## 四、RLock 接口核心方法

| 方法签名 | 功能说明 |
|---------|---------|
| `lock()` | 阻塞获取锁，默认30秒过期，启用Watch Dog |
| `lock(long leaseTime, TimeUnit unit)` | 阻塞获取锁，指定过期时间，**禁用Watch Dog** |
| `tryLock()` | 尝试获取锁，立即返回 boolean |
| `tryLock(long waitTime, long leaseTime, TimeUnit unit)` | 带等待时间的尝试获取 |
| `unlock()` | 释放锁 |
| `isHeldByCurrentThread()` | 检查当前线程是否持有锁 |
| `lockInterruptibly()` | 可中断的锁获取 |

---

## 五、代码优化建议

### 5.1 当前代码的问题

```java
lock.lock();                    // 使用默认30秒过期
Thread.sleep(60_000L);         // 业务执行60秒
```

**潜在问题**：
- 业务时间明确（60秒），却依赖 Watch Dog 自动延长
- 增加了定时任务开销

### 5.2 优化方案

**方案一：直接指定过期时间（推荐）**

```java
// 明确业务执行时间，禁用Watch Dog
lock.lock(60, TimeUnit.SECONDS);
try {
    System.out.println("get lock success");
    Thread.sleep(60_000L);
    System.out.println("done");
} finally {
    if (lock.isHeldByCurrentThread()) {
        lock.unlock();
    }
}
```

**方案二：使用 tryLock 带超时**

```java
// 最多等待5秒获取锁，锁持有60秒
boolean locked = lock.tryLock(5, 60, TimeUnit.SECONDS);
if (locked) {
    try {
        // 业务逻辑
    } finally {
        lock.unlock();
    }
} else {
    // 获取锁失败处理
}
```

---

## 六、关键要点总结

| 要点 | 说明 |
|------|------|
| **锁实现类** | `RedissonLock`（默认） |
| **锁类型** | 可重入分布式锁 |
| **Watch Dog** | 自动延长锁过期时间，防止业务未完成时锁被释放 |
| **默认超时** | 30秒 |
| **定时刷新间隔** | 10秒（超时时间的1/3） |
| **禁用方式** | 使用 `lock(long leaseTime, TimeUnit unit)` 指定过期时间 |
| **底层数据结构** | Redis Hash（支持重入计数） |

---

## 七、适用场景与注意事项

### 适用场景
- 分布式环境下的资源互斥访问
- 业务执行时间不确定的场景（需要 Watch Dog）
- 需要可重入锁的场景

### 注意事项
1. **Redis 单点风险**：生产环境建议使用哨兵或集群模式
2. **锁过期时间设置**：根据业务实际执行时间合理设置
3. **锁释放时机**：必须在 finally 块中释放锁，防止死锁
4. **Redisson 版本**：建议使用最新稳定版本

---

**文档生成时间**：2026-05-09  
**分析对象**：`src/main/java/com/java8/redisson/RedissDemo.java`