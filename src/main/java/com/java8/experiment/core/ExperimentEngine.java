package com.java8.experiment.core;

import com.java8.experiment.model.Bucket;
import com.java8.experiment.model.Experiment;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实验分流引擎
 */
@Slf4j
public class ExperimentEngine {

    private final MurmurHashBucketer bucketer;
    private final Integer totalBuckets;

    // 实验层映射: layerName -> LayerManager
    private final Map<String, LayerManager> layers = new ConcurrentHashMap<>();

    public ExperimentEngine(Integer totalBuckets) {
        this.totalBuckets = totalBuckets;
        this.bucketer = new MurmurHashBucketer(totalBuckets);
    }

    /**
     * 添加实验层
     */
    public void addLayer(String layerName) {
        layers.putIfAbsent(layerName, new LayerManager(layerName, totalBuckets));
    }

    /**
     * 注册实验
     */
    public void registerExperiment(Experiment experiment) {
        LayerManager layer = layers.get(experiment.getLayerName());
        if (layer == null) {
            throw new IllegalArgumentException("实验层不存在: " + experiment.getLayerName());
        }
        layer.registerExperiment(experiment);
    }

    /**
     * 为用户分配实验 - 核心分流方法
     * @param userId 用户ID
     * @param layerName 实验层
     * @return 分配结果，包含实验信息和桶配置
     */
    public ExperimentAssignment assignExperiment(String userId, String layerName) {
        LayerManager layer = layers.get(layerName);
        if (layer == null) {
            return ExperimentAssignment.empty(layerName, "层不存在");
        }

        // 1. 计算用户在该层的桶号
        int bucketNumber = bucketer.calculateBucket(userId, layer.getLayerSalt());

        // 2. 查找该桶号属于哪个实验
        for (Experiment experiment : layer.getExperiments().values()) {
            // 检查实验状态
            if (experiment.getStatus() != Experiment.Status.RUNNING) {
                continue;
            }

            // 查找匹配的桶
            for (Bucket bucket : experiment.getBuckets()) {
                if (bucketNumber >= bucket.getStart() && bucketNumber <= bucket.getEnd()) {
                    System.out.println(String.format("用户 %s 在层 %s 命中实验 %s，桶: %s",
                            userId, layerName, experiment.getExperimentId(), bucket.getBucketName()));

                    return ExperimentAssignment.builder()
                            .userId(userId)
                            .hit(true)
                            .layerName(layerName)
                            .experimentId(experiment.getExperimentId())
                            .bucketId(bucket.getBucketId())
                            .bucketName(bucket.getBucketName())
                            .bucketNumber(bucketNumber)
                            .config(bucket.getConfig())
                            .build();
                }
            }
        }

        // 未命中任何实验（空白对照）
        return ExperimentAssignment.empty(layerName, "未命中实验");
    }

    /**
     * 批量分配 - 同时获取用户在多个层的实验
     */
    public Map<String, ExperimentAssignment> batchAssign(String userId, String... layerNames) {
        Map<String, ExperimentAssignment> results = new ConcurrentHashMap<>();
        for (String layerName : layerNames) {
            results.put(layerName, assignExperiment(userId, layerName));
        }
        return results;
    }
}