package com.java8.experiment.core;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 基于MurmurHash的分桶器
 */
@Slf4j
public class MurmurHashBucketer {

    private final Integer totalBuckets;

    public MurmurHashBucketer(Integer totalBuckets) {
        this.totalBuckets = totalBuckets;
    }

    /**
     * 计算用户所属的桶号
     *
     * @param userId    用户ID
     * @param layerSalt 层盐值，保证不同层正交
     * @return 桶号 (0 到 totalBuckets-1)
     */
    public Integer calculateBucket(String userId, String layerSalt) {
        // 组合用户ID和层盐值
        String input = userId + ":" + layerSalt;

        // 使用Guava的MurmurHash3_32实现
        int hash = Hashing.murmur3_32_fixed()
                .hashString(input, StandardCharsets.UTF_8)
                .asInt();

        // 取绝对值并取模
        int bucket = Math.abs(hash) % totalBuckets;

        System.out.println(String.format("用户 %s 在层 %s 的桶号为: %d", userId, layerSalt, bucket));
        return bucket;
    }

    /**
     * 使用实验参数作为额外盐值，实现实验内的细粒度分流
     */
    public Integer calculateBucketWithParam(String userId, String layerSalt, String paramSalt) {
        String input = userId + ":" + layerSalt + ":" + paramSalt;
        int hash = Hashing.murmur3_32_fixed()
                .hashString(input, StandardCharsets.UTF_8)
                .asInt();
        return Math.abs(hash) % totalBuckets;
    }
}