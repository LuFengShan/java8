package com.java8.experiment.model;

import com.java8.experiment.model.Bucket;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 实验配置
 */
@Data
@Builder
public class Experiment {
    private String experimentId;        // 实验ID
    private String experimentName;       // 实验名称
    private String layerName;           // 所属层名称
    private Integer trafficAllocation;   // 实验分配流量百分比 (1-100)
    private List<Bucket> buckets;        // 实验包含的桶范围
    private Map<String, Object> params;  // 实验参数配置
    private Status status;               // 实验状态

    public enum Status {
        RUNNING, PAUSED, DRAFT
    }
}
