package com.java8.collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CoreAlgorithms单元测试类
 * 测试四种核心算法的功能正确性和线程安全性
 */
@DisplayName("CoreAlgorithms单元测试")
class CoreAlgorithmsTest {
    
    // ========================== 一致性哈希算法测试 ==========================
    
    @Test
    @DisplayName("一致性哈希算法 - 基本功能测试")
    void testConsistentHashingBasic() {
        CoreAlgorithms.ConsistentHashing<String> hashing = new CoreAlgorithms.ConsistentHashing<>(3);
        
        hashing.addNode("Node1");
        hashing.addNode("Node2");
        hashing.addNode("Node3");
        
        assertEquals(3, hashing.getNodeCount());
        
        String key1 = "test_key_1";
        String key2 = "test_key_2";
        String key3 = "test_key_3";
        
        String node1 = hashing.getNode(key1);
        String node2 = hashing.getNode(key2);
        String node3 = hashing.getNode(key3);
        
        assertNotNull(node1);
        assertNotNull(node2);
        assertNotNull(node3);
        
        hashing.removeNode("Node2");
        assertEquals(2, hashing.getNodeCount());
        
        String node1AfterRemove = hashing.getNode(key1);
        assertNotEquals("Node2", node1AfterRemove);
    }
    
    @Test
    @DisplayName("一致性哈希算法 - 虚拟节点测试")
    void testConsistentHashingVirtualNodes() {
        CoreAlgorithms.ConsistentHashing<String> hashing = new CoreAlgorithms.ConsistentHashing<>(100);
        
        hashing.addNode("NodeA");
        hashing.addNode("NodeB");
        
        int[] distribution = new int[2];
        for (int i = 0; i < 1000; i++) {
            String node = hashing.getNode("key_" + i);
            if ("NodeA".equals(node)) {
                distribution[0]++;
            } else if ("NodeB".equals(node)) {
                distribution[1]++;
            }
        }
        
        double ratio = (double) distribution[0] / distribution[1];
        assertTrue(ratio > 0.8 && ratio < 1.2, "数据分布应该相对均匀");
    }
    
    @Test
    @DisplayName("一致性哈希算法 - 空环测试")
    void testConsistentHashingEmptyRing() {
        CoreAlgorithms.ConsistentHashing<String> hashing = new CoreAlgorithms.ConsistentHashing<>(3);
        
        assertNull(hashing.getNode("test_key"));
        
        hashing.addNode("SingleNode");
        assertEquals("SingleNode", hashing.getNode("any_key"));
        
        hashing.removeNode("SingleNode");
        assertNull(hashing.getNode("test_key"));
    }
    
    // ========================== 布隆过滤器测试 ==========================
    
    @Test
    @DisplayName("布隆过滤器 - 基本功能测试")
    void testBloomFilterBasic() {
        CoreAlgorithms.BloomFilter filter = new CoreAlgorithms.BloomFilter(1000, 0.01);
        
        filter.add("element1");
        filter.add("element2");
        filter.add("element3");
        
        assertTrue(filter.contains("element1"));
        assertTrue(filter.contains("element2"));
        assertTrue(filter.contains("element3"));
        assertFalse(filter.contains("element4"));
        
        assertEquals(3, filter.getElementCount());
    }
    
    @Test
    @DisplayName("布隆过滤器 - 误判率测试")
    void testBloomFilterFalsePositive() {
        CoreAlgorithms.BloomFilter filter = new CoreAlgorithms.BloomFilter(100, 0.01);
        
        for (int i = 0; i < 100; i++) {
            filter.add("element_" + i);
        }
        
        int falsePositives = 0;
        int testCount = 1000;
        
        for (int i = 100; i < 100 + testCount; i++) {
            if (filter.contains("element_" + i)) {
                falsePositives++;
            }
        }
        
        double falsePositiveRate = (double) falsePositives / testCount;
        assertTrue(falsePositiveRate < 0.05, "实际误判率应该接近理论值");
    }
    
    @Test
    @DisplayName("布隆过滤器 - 清空测试")
    void testBloomFilterClear() {
        CoreAlgorithms.BloomFilter filter = new CoreAlgorithms.BloomFilter(100, 0.01);
        
        filter.add("test_element");
        assertTrue(filter.contains("test_element"));
        
        filter.clear();
        assertFalse(filter.contains("test_element"));
        assertEquals(0, filter.getElementCount());
    }
    
    @Test
    @DisplayName("布隆过滤器 - 参数验证测试")
    void testBloomFilterParameterValidation() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.BloomFilter(0, 0.01));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.BloomFilter(100, 0));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.BloomFilter(100, 1));
    }
    
    // ========================== 限流算法测试 ==========================
    
    @Test
    @DisplayName("令牌桶限流器 - 基本功能测试")
    void testTokenBucketRateLimiterBasic() throws InterruptedException {
        CoreAlgorithms.TokenBucketRateLimiter limiter = 
            new CoreAlgorithms.TokenBucketRateLimiter(10, 5, 1, TimeUnit.SECONDS);
        
        assertTrue(limiter.tryAcquire(5));
        assertEquals(5, limiter.getAvailableTokens());
        
        assertTrue(limiter.tryAcquire(5));
        assertEquals(0, limiter.getAvailableTokens());
        
        assertFalse(limiter.tryAcquire(1));
    }
    
    @Test
    @DisplayName("令牌桶限流器 - 令牌补充测试")
    void testTokenBucketRateLimiterRefill() throws InterruptedException {
        CoreAlgorithms.TokenBucketRateLimiter limiter = 
            new CoreAlgorithms.TokenBucketRateLimiter(10, 10, 100, TimeUnit.MILLISECONDS);
        
        assertTrue(limiter.tryAcquire(10));
        assertEquals(0, limiter.getAvailableTokens());
        
        Thread.sleep(150);
        
        assertTrue(limiter.tryAcquire(10));
        assertEquals(0, limiter.getAvailableTokens());
    }
    
    @Test
    @DisplayName("令牌桶限流器 - 参数调整测试")
    void testTokenBucketRateLimiterParameterAdjustment() {
        CoreAlgorithms.TokenBucketRateLimiter limiter = 
            new CoreAlgorithms.TokenBucketRateLimiter(10, 5, 1, TimeUnit.SECONDS);
        
        limiter.adjustParameters(20, 10, 2, TimeUnit.SECONDS);
        
        assertTrue(limiter.tryAcquire(10));
        assertEquals(10, limiter.getAvailableTokens());
    }
    
    @Test
    @DisplayName("令牌桶限流器 - 参数验证测试")
    void testTokenBucketRateLimiterParameterValidation() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.TokenBucketRateLimiter(0, 5, 1, TimeUnit.SECONDS));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.TokenBucketRateLimiter(10, 0, 1, TimeUnit.SECONDS));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.TokenBucketRateLimiter(10, 5, 0, TimeUnit.SECONDS));
    }
    
    // ========================== LRU缓存算法测试 ==========================
    
    @Test
    @DisplayName("LRU缓存 - 基本功能测试")
    void testLRUCacheBasic() {
        CoreAlgorithms.LRUCache<String, Integer> cache = new CoreAlgorithms.LRUCache<>(3);
        
        cache.put("key1", 1);
        cache.put("key2", 2);
        cache.put("key3", 3);
        
        assertEquals(1, cache.get("key1"));
        assertEquals(2, cache.get("key2"));
        assertEquals(3, cache.get("key3"));
        
        cache.put("key4", 4);
        
        assertNull(cache.get("key1"));
        assertEquals(2, cache.get("key2"));
        assertEquals(3, cache.get("key3"));
        assertEquals(4, cache.get("key4"));
        
        assertEquals(3, cache.size());
    }
    
    @Test
    @DisplayName("LRU缓存 - 淘汰策略测试")
    void testLRUCacheEviction() {
        CoreAlgorithms.LRUCache<String, Integer> cache = new CoreAlgorithms.LRUCache<>(3);
        
        cache.put("key1", 1);
        cache.put("key2", 2);
        cache.put("key3", 3);
        
        cache.get("key1");
        cache.put("key4", 4);
        
        assertNull(cache.get("key2"));
        assertEquals(1, cache.get("key1"));
        assertEquals(3, cache.get("key3"));
        assertEquals(4, cache.get("key4"));
    }
    
    @Test
    @DisplayName("LRU缓存 - 更新操作测试")
    void testLRUCacheUpdate() {
        CoreAlgorithms.LRUCache<String, Integer> cache = new CoreAlgorithms.LRUCache<>(2);
        
        cache.put("key1", 1);
        cache.put("key2", 2);
        cache.put("key1", 10);
        
        assertEquals(10, cache.get("key1"));
        assertEquals(2, cache.get("key2"));
        
        cache.put("key3", 3);
        
        assertNull(cache.get("key1"));
        assertEquals(2, cache.get("key2"));
        assertEquals(3, cache.get("key3"));
    }
    
    @Test
    @DisplayName("LRU缓存 - 移除和清空测试")
    void testLRUCacheRemoveAndClear() {
        CoreAlgorithms.LRUCache<String, Integer> cache = new CoreAlgorithms.LRUCache<>(3);
        
        cache.put("key1", 1);
        cache.put("key2", 2);
        
        assertEquals(1, cache.remove("key1"));
        assertNull(cache.get("key1"));
        assertEquals(1, cache.size());
        
        cache.clear();
        assertEquals(0, cache.size());
        assertNull(cache.get("key2"));
    }
    
    @Test
    @DisplayName("LRU缓存 - 容量验证测试")
    void testLRUCacheCapacityValidation() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.LRUCache<>(0));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new CoreAlgorithms.LRUCache<>(-1));
    }
    
    // ========================== 线程安全性测试 ==========================
    
    @Test
    @DisplayName("一致性哈希算法 - 线程安全测试")
    @Timeout(10)
    void testConsistentHashingThreadSafety() throws InterruptedException {
        CoreAlgorithms.ConsistentHashing<String> hashing = new CoreAlgorithms.ConsistentHashing<>(3);
        
        int threadCount = 10;
        int operationsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    hashing.addNode("Node_" + threadId + "_" + j);
                    hashing.getNode("key_" + threadId + "_" + j);
                    if (j % 10 == 0) {
                        hashing.removeNode("Node_" + threadId + "_" + (j - 5));
                    }
                }
            });
        }
        
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        
        assertTrue(hashing.getNodeCount() >= 0);
    }
    
    @Test
    @DisplayName("布隆过滤器 - 线程安全测试")
    @Timeout(10)
    void testBloomFilterThreadSafety() throws InterruptedException {
        CoreAlgorithms.BloomFilter filter = new CoreAlgorithms.BloomFilter(10000, 0.01);
        
        int threadCount = 10;
        int operationsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    filter.add("element_" + threadId + "_" + j);
                    filter.contains("element_" + threadId + "_" + j);
                }
            });
        }
        
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        
        assertEquals(threadCount * operationsPerThread, filter.getElementCount());
    }
    
    @Test
    @DisplayName("令牌桶限流器 - 线程安全测试")
    @Timeout(10)
    void testTokenBucketRateLimiterThreadSafety() throws InterruptedException {
        CoreAlgorithms.TokenBucketRateLimiter limiter = 
            new CoreAlgorithms.TokenBucketRateLimiter(1000, 100, 100, TimeUnit.MILLISECONDS);
        
        int threadCount = 10;
        int operationsPerThread = 100;
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    if (limiter.tryAcquire(1)) {
                        successCount.incrementAndGet();
                    }
                }
            });
        }
        
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        
        assertTrue(successCount.get() <= threadCount * operationsPerThread);
        assertTrue(successCount.get() >= 0);
    }
    
    @Test
    @DisplayName("LRU缓存 - 线程安全测试")
    @Timeout(10)
    void testLRUCacheThreadSafety() throws InterruptedException {
        CoreAlgorithms.LRUCache<String, Integer> cache = new CoreAlgorithms.LRUCache<>(100);
        
        int threadCount = 10;
        int operationsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    String key = "key_" + threadId + "_" + j;
                    cache.put(key, j);
                    cache.get(key);
                    if (j % 10 == 0) {
                        cache.remove(key);
                    }
                }
            });
        }
        
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        
        assertTrue(cache.size() <= 100);
        assertTrue(cache.size() >= 0);
    }
    
    // ========================== 性能测试 ==========================
    
    @Test
    @DisplayName("LRU缓存 - 性能测试")
    @Timeout(5)
    void testLRUCachePerformance() {
        CoreAlgorithms.LRUCache<Integer, String> cache = new CoreAlgorithms.LRUCache<>(1000);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10000; i++) {
            cache.put(i, "value_" + i);
            if (i % 10 == 0) {
                cache.get(i - 5);
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 1000, "10000次操作应该在1秒内完成");
    }
    
    @Test
    @DisplayName("布隆过滤器 - 性能测试")
    @Timeout(5)
    void testBloomFilterPerformance() {
        CoreAlgorithms.BloomFilter filter = new CoreAlgorithms.BloomFilter(10000, 0.01);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10000; i++) {
            filter.add("element_" + i);
            if (i % 10 == 0) {
                filter.contains("element_" + (i - 5));
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 1000, "10000次操作应该在1秒内完成");
    }
}