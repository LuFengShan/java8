package com.java8.experiment.core;

import com.java8.experiment.model.Experiment;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 实验层管理器
 */
@Slf4j
public class LayerManager {

    // 每个层都有独立的盐值
    @Getter
    private final String layerName;
    @Getter
    private final String layerSalt;
    @Getter
    private final Integer totalBuckets;  // 总桶数，通常是100或1000

    // 该层内的实验映射
    @Getter
    private final ConcurrentHashMap<String, Experiment> experiments = new ConcurrentHashMap<>();

    public LayerManager(String layerName, Integer totalBuckets) {
        this.layerName = layerName;
        this.layerSalt = "layer_" + layerName + "_salt";
        this.totalBuckets = totalBuckets;
    }

    /**
     * 注册实验到该层
     */
    public void registerExperiment(Experiment experiment) {
        if (!layerName.equals(experiment.getLayerName())) {
            throw new IllegalArgumentException("实验不属于当前层");
        }

        // 检查桶范围是否冲突
        validateBucketConflict(experiment);

        experiments.put(experiment.getExperimentId(), experiment);
        System.out.println(String.format("实验 %s 注册到层 %s, 桶范围: %s-%s",
                experiment.getExperimentId(), layerName,
                experiment.getBuckets().get(0).getStart(),
                experiment.getBuckets().get(0).getEnd()));
    }

    /**
     * 检查同一层内实验桶范围是否有冲突
     */
    private void validateBucketConflict(Experiment newExperiment) {
        experiments.values().forEach(existing -> {
            newExperiment.getBuckets().forEach(newBucket -> {
                existing.getBuckets().forEach(existingBucket -> {
                    if (rangesOverlap(newBucket.getStart(), newBucket.getEnd(),
                            existingBucket.getStart(), existingBucket.getEnd())) {
                        throw new IllegalArgumentException(
                                String.format("实验 %s 与实验 %s 桶范围冲突",
                                        newExperiment.getExperimentId(), existing.getExperimentId()));
                    }
                });
            });
        });
    }

    private boolean rangesOverlap(int start1, int end1, int start2, int end2) {
        return !(end1 < start2 || end2 < start1);
    }
}