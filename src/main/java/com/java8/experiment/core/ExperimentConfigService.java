package com.java8.experiment.core;

import com.java8.experiment.model.Bucket;
import com.java8.experiment.model.Experiment;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 实验配置服务 - 模拟从配置中心加载实验
 */
@Slf4j
public class ExperimentConfigService {

    private final ExperimentEngine experimentEngine;

    public ExperimentConfigService(ExperimentEngine experimentEngine) {
        this.experimentEngine = experimentEngine;
    }

    @PostConstruct
    public void initExperiments() {
        // 创建实验层
        experimentEngine.addLayer("ui_layer");
        experimentEngine.addLayer("algorithm_layer");
        experimentEngine.addLayer("price_layer");

        // 配置UI层实验
        configureUILayerExperiments();

        // 配置算法层实验
        configureAlgorithmLayerExperiments();

        log.info("实验配置加载完成");
    }

    private void configureUILayerExperiments() {
        // UI层 - 按钮颜色实验 (10%流量)
        Experiment buttonColorExp = Experiment.builder()
                .experimentId("exp_ui_001")
                .experimentName("button_color_test")
                .layerName("ui_layer")
                .trafficAllocation(50)
                .buckets(Arrays.asList(
                        Bucket.builder()
                                .bucketId("bucket_control")
                                .bucketName("control")
                                .start(0)
                                .end(24)
                                .config(new HashMap<String, Object>() {{
                                    put("button_color", "#3366FF");
                                    put("button_size", "medium");
                                }})
                                .build(),
                        Bucket.builder()
                                .bucketId("bucket_treatment")
                                .bucketName("treatment")
                                .start(25)
                                .end(49)
                                .config(new HashMap<String, Object>() {{
                                    put("button_color", "#FF3366");
                                    put("button_size", "large");
                                }})
                                .build()
                ))
                .status(Experiment.Status.RUNNING)
                .build();

        experimentEngine.registerExperiment(buttonColorExp);

        // 可以继续配置其他UI实验...
    }

    private void configureAlgorithmLayerExperiments() {
        // 算法层 - 推荐算法实验 (20%流量)
        Experiment algorithmExp = Experiment.builder()
                .experimentId("exp_algo_001")
                .experimentName("recommendation_algorithm_test")
                .layerName("algorithm_layer")
                .trafficAllocation(20)
                .buckets(Arrays.asList(
                        Bucket.builder()
                                .bucketId("bucket_v1")
                                .bucketName("cf_algorithm")
                                .start(0)
                                .end(9)
                                .config(new HashMap<String, Object>() {{
                                    put("algorithm", "collaborative_filtering");
                                    put("recall_size", 100);
                                }})
                                .build(),
                        Bucket.builder()
                                .bucketId("bucket_v2")
                                .bucketName("deepfm_algorithm")
                                .start(10)
                                .end(19)
                                .config(new HashMap<String, Object>() {{
                                    put("algorithm", "deepfm");
                                    put("recall_size", 150);
                                }})
                                .build()
                ))
                .status(Experiment.Status.RUNNING)
                .build();

        experimentEngine.registerExperiment(algorithmExp);
    }
}