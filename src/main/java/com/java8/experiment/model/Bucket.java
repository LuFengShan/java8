package com.java8.experiment.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 桶定义
 */
@Data
@Builder
public class Bucket {
    private String bucketId;      // 桶ID
    private String bucketName;    // 桶名称 (如: control, treatment1)
    private Integer start;         // 起始桶号 (包含)
    private Integer end;           // 结束桶号 (包含)
    private Map<String, Object> config; // 桶配置
}
