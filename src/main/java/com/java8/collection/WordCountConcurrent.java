package com.java8.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class WordCountConcurrent {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] sentences = {"hello world", "hello go"};

        // 创建固定大小的线程池（可根据句子数量调整）
        ExecutorService executor = Executors.newFixedThreadPool(sentences.length);

        // 存放每个句子统计结果的 Future
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();

        // 提交任务：每个句子交给一个线程处理
        for (String sentence : sentences) {
            futures.add(executor.submit(new WordCountTask(sentence)));
        }

        // 合并最终结果
        Map<String, Integer> totalCount = new HashMap<>();
        for (Future<Map<String, Integer>> future : futures) {
            Map<String, Integer> partial = future.get(); // 阻塞等待结果
            // 合并到总结果中
            for (Map.Entry<String, Integer> entry : partial.entrySet()) {
                totalCount.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        // 关闭线程池
        executor.shutdown();

        // 输出结果
        System.out.println(totalCount);
    }

    // 单词统计任务
    static class WordCountTask implements Callable<Map<String, Integer>> {
        private final String sentence;

        public WordCountTask(String sentence) {
            this.sentence = sentence;
        }

        @Override
        public Map<String, Integer> call() {
            Map<String, Integer> map = new HashMap<>();
            // 按空格分割单词（若句子中有多个空格，split("\\s+") 更鲁棒）
            String[] words = sentence.split("\\s+");
            for (String word : words) {
                map.merge(word, 1, Integer::sum);
            }
            return map;
        }
    }
}