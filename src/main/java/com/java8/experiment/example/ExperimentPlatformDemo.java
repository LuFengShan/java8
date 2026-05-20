package com.java8.experiment.example;

import com.java8.experiment.core.ExperimentAssignment;
import com.java8.experiment.core.ExperimentConfigService;
import com.java8.experiment.core.ExperimentEngine;

import java.util.Map;
import java.util.Optional;

/**
 * 实验平台使用示例
 */
public class ExperimentPlatformDemo {

    public static void main(String[] args) {
        // 1. 初始化引擎 (总桶数100)
        ExperimentEngine engine = new ExperimentEngine(100);

        // 2. 加载实验配置
        ExperimentConfigService configService = new ExperimentConfigService(engine);
        configService.initExperiments();

        // 3. 模拟用户请求
        String userId = "e9acbf283d9541549ef1708fcdb76f46";
        //String userId = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT);
        System.out.println(userId);

        // 4. 为用户分配实验 (单层)
        ExperimentAssignment uiAssignment = engine.assignExperiment(userId, "ui_layer");

        if (uiAssignment.isHit()) {
            System.out.println("命中UI实验: " + uiAssignment.getExperimentId());
            System.out.println("实验组: " + uiAssignment.getBucketName());

            // 获取实验配置
            Optional<String> buttonColor = uiAssignment.getConfigValue("button_color", String.class);
            buttonColor.ifPresent(color ->
                    System.out.println("按钮颜色配置为: " + color)
            );
        } else {
            System.out.println("用户未命中UI层实验: " + uiAssignment.getMessage());
        }

        // 5. 批量分配 (同时获取用户在多个层的实验)
        Map<String, ExperimentAssignment> assignments = engine.batchAssign(
                userId, "ui_layer", "algorithm_layer", "price_layer"
        );

        // 6. 在业务代码中使用
        processUserRequest(userId, assignments);
    }

    /**
     * 模拟业务处理
     */
    private static void processUserRequest(String userId, Map<String, ExperimentAssignment> assignments) {
        // UI渲染
        ExperimentAssignment uiAssign = assignments.get("ui_layer");
        if (uiAssign.isHit()) {
            String buttonColor = uiAssign.getConfigValue("button_color", String.class)
                    .orElse("#3366FF"); // 默认蓝色
            renderButton(buttonColor);
        }

        // 算法推荐
        ExperimentAssignment algoAssign = assignments.get("algorithm_layer");
        if (algoAssign.isHit()) {
            String algorithm = algoAssign.getConfigValue("algorithm", String.class)
                    .orElse("default");
            int recallSize = algoAssign.getConfigValue("recall_size", Integer.class)
                    .orElse(50);
            generateRecommendations(userId, algorithm, recallSize);
        }
    }

    private static void renderButton(String color) {
        System.out.println("渲染按钮，颜色: " + color);
    }

    private static void generateRecommendations(String userId, String algorithm, int recallSize) {
        System.out.println("为用户 " + userId + " 使用 " + algorithm + " 算法，召回 " + recallSize + " 条内容");
    }
}
