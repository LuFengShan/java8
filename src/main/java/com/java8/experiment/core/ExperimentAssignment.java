package com.java8.experiment.core;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

/**
 * 实验分配结果
 */
@Data
@Builder
public class ExperimentAssignment {
    private String userId;
    private String layerName;
    private String experimentId;
    private String bucketId;
    private String bucketName;
    private Integer bucketNumber;
    private Map<String, Object> config;
    private boolean hit;  // 是否命中实验
    private String message;

    public static ExperimentAssignment empty(String layerName, String message) {
        return ExperimentAssignment.builder()
                .layerName(layerName)
                .hit(false)
                .message(message)
                .build();
    }

    /**
     * 获取实验配置值
     */
    public <T> Optional<T> getConfigValue(String key, Class<T> type) {
        if (config != null && config.containsKey(key)) {
            return Optional.of(type.cast(config.get(key)));
        }
        return Optional.empty();
    }
}