package com.java8.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissDemo {

    private static RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setPassword(null);
        config.setLockWatchdogTimeout(30_000L);
        redissonClient = Redisson.create(config);
    }

    public static void main(String[] args) {
        RLock lock = redissonClient.getLock("sgxLock");
        try {
            lock.lock();
            System.out.println("get lock success");
            Thread.sleep(60_000L);
            System.out.println("done");
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("释放锁，watch dog stop");
            }
        }
        redissonClient.shutdown();

    }

}
