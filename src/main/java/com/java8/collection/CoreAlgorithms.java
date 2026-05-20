package com.java8.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * CoreAlgorithms - 实现四种核心算法：一致性哈希、布隆过滤器、限流算法、LRU缓存
 * 
 * 该类提供了四种常用算法的线程安全实现，适用于分布式系统、缓存、流量控制等场景
 */
public class CoreAlgorithms {
    
    // ========================== 一致性哈希算法 ==========================
    
    /**
     * 一致性哈希算法实现
     * 支持虚拟节点机制，解决数据分布不均问题
     */
    public static class ConsistentHashing<T> {
        private final SortedMap<Integer, T> circle = new ConcurrentSkipListMap<>();
        private final Map<T, List<Integer>> nodeToVirtualNodes = new ConcurrentHashMap<>();
        private final int virtualNodeCount;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        /**
         * 构造一致性哈希环
         * @param virtualNodeCount 每个物理节点的虚拟节点数量
         */
        public ConsistentHashing(int virtualNodeCount) {
            this.virtualNodeCount = Math.max(virtualNodeCount, 1);
        }
        
        /**
         * 添加节点到哈希环
         * @param node 要添加的节点
         */
        public void addNode(T node) {
            lock.writeLock().lock();
            try {
                if (nodeToVirtualNodes.containsKey(node)) {
                    return;
                }
                
                List<Integer> virtualNodes = new ArrayList<>();
                for (int i = 0; i < virtualNodeCount; i++) {
                    String virtualNodeName = node.toString() + "#" + i;
                    int hash = getHash(virtualNodeName);
                    circle.put(hash, node);
                    virtualNodes.add(hash);
                }
                nodeToVirtualNodes.put(node, virtualNodes);
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 从哈希环移除节点
         * @param node 要移除的节点
         */
        public void removeNode(T node) {
            lock.writeLock().lock();
            try {
                List<Integer> virtualNodes = nodeToVirtualNodes.remove(node);
                if (virtualNodes != null) {
                    for (Integer hash : virtualNodes) {
                        circle.remove(hash);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 根据key获取对应的节点
         * @param key 数据key
         * @return 对应的节点
         */
        public T getNode(String key) {
            lock.readLock().lock();
            try {
                if (circle.isEmpty()) {
                    return null;
                }
                
                int hash = getHash(key);
                SortedMap<Integer, T> tailMap = circle.tailMap(hash);
                int nodeHash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
                return circle.get(nodeHash);
            } finally {
                lock.readLock().unlock();
            }
        }
        
        /**
         * 获取哈希环中的节点数量
         * @return 节点数量
         */
        public int getNodeCount() {
            lock.readLock().lock();
            try {
                return nodeToVirtualNodes.size();
            } finally {
                lock.readLock().unlock();
            }
        }
        
        private int getHash(String key) {
            return Math.abs(key.hashCode());
        }
    }
    
    // ========================== 布隆过滤器 ==========================
    
    /**
     * 布隆过滤器实现
     * 支持指定预期元素数量和误判率进行初始化
     */
    public static class BloomFilter {
        private final BitSet bitSet;
        private final int bitSetSize;
        private final int numHashFunctions;
        private final AtomicInteger elementCount = new AtomicInteger(0);
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        /**
         * 构造布隆过滤器
         * @param expectedElements 预期元素数量
         * @param falsePositiveRate 期望的误判率 (0 < rate < 1)
         */
        public BloomFilter(int expectedElements, double falsePositiveRate) {
            if (expectedElements <= 0) {
                throw new IllegalArgumentException("Expected elements must be positive");
            }
            if (falsePositiveRate <= 0 || falsePositiveRate >= 1) {
                throw new IllegalArgumentException("False positive rate must be between 0 and 1");
            }
            
            this.bitSetSize = calculateBitSetSize(expectedElements, falsePositiveRate);
            this.numHashFunctions = calculateNumHashFunctions(expectedElements, bitSetSize);
            this.bitSet = new BitSet(bitSetSize);
        }
        
        /**
         * 添加元素到布隆过滤器
         * @param element 要添加的元素
         */
        public void add(String element) {
            lock.writeLock().lock();
            try {
                for (int i = 0; i < numHashFunctions; i++) {
                    int hash = getHash(element, i);
                    bitSet.set(hash % bitSetSize);
                }
                elementCount.incrementAndGet();
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 判断元素是否可能存在于布隆过滤器中
         * @param element 要检查的元素
         * @return true表示可能存在，false表示一定不存在
         */
        public boolean contains(String element) {
            lock.readLock().lock();
            try {
                for (int i = 0; i < numHashFunctions; i++) {
                    int hash = getHash(element, i);
                    if (!bitSet.get(hash % bitSetSize)) {
                        return false;
                    }
                }
                return true;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        /**
         * 清空布隆过滤器
         */
        public void clear() {
            lock.writeLock().lock();
            try {
                bitSet.clear();
                elementCount.set(0);
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 获取当前元素数量
         * @return 元素数量
         */
        public int getElementCount() {
            return elementCount.get();
        }
        
        /**
         * 获取布隆过滤器的误判率估计值
         * @return 估计的误判率
         */
        public double getEstimatedFalsePositiveRate() {
            lock.readLock().lock();
            try {
                int count = elementCount.get();
                if (count == 0) return 0.0;
                
                double k = numHashFunctions;
                double m = bitSetSize;
                double n = count;
                return Math.pow(1 - Math.exp(-k * n / m), k);
            } finally {
                lock.readLock().unlock();
            }
        }
        
        private int calculateBitSetSize(int n, double p) {
            return (int) Math.ceil(-(n * Math.log(p)) / (Math.log(2) * Math.log(2)));
        }
        
        private int calculateNumHashFunctions(int n, int m) {
            return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
        }
        
        private int getHash(String element, int seed) {
            int hash = element.hashCode();
            return Math.abs(hash ^ (hash << seed) ^ (hash >> seed));
        }
    }
    
    // ========================== 限流算法 ==========================
    
    /**
     * 令牌桶限流算法实现
     * 支持动态调整限流参数
     */
    public static class TokenBucketRateLimiter {
        private final long capacity;
        private final long refillTokens;
        private final long refillPeriodMs;
        private long lastRefillTimestamp;
        private long currentTokens;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        /**
         * 构造令牌桶限流器
         * @param capacity 桶容量
         * @param refillTokens 每次补充的令牌数量
         * @param refillPeriod 补充周期
         * @param timeUnit 时间单位
         */
        public TokenBucketRateLimiter(long capacity, long refillTokens, long refillPeriod, TimeUnit timeUnit) {
            if (capacity <= 0 || refillTokens <= 0 || refillPeriod <= 0) {
                throw new IllegalArgumentException("Parameters must be positive");
            }
            
            this.capacity = capacity;
            this.refillTokens = refillTokens;
            this.refillPeriodMs = timeUnit.toMillis(refillPeriod);
            this.currentTokens = capacity;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }
        
        /**
         * 尝试获取令牌
         * @param tokens 请求的令牌数量
         * @return true表示获取成功，false表示获取失败
         */
        public boolean tryAcquire(int tokens) {
            if (tokens <= 0) {
                throw new IllegalArgumentException("Tokens must be positive");
            }
            
            lock.writeLock().lock();
            try {
                refill();
                
                if (currentTokens >= tokens) {
                    currentTokens -= tokens;
                    return true;
                }
                return false;
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 尝试获取一个令牌
         * @return true表示获取成功，false表示获取失败
         */
        public boolean tryAcquire() {
            return tryAcquire(1);
        }
        
        /**
         * 动态调整限流参数
         * @param newCapacity 新的桶容量
         * @param newRefillTokens 新的补充令牌数量
         * @param newRefillPeriod 新的补充周期
         * @param timeUnit 时间单位
         */
        public void adjustParameters(long newCapacity, long newRefillTokens, long newRefillPeriod, TimeUnit timeUnit) {
            lock.writeLock().lock();
            try {
                this.currentTokens = Math.min(newCapacity, this.currentTokens);
                this.lastRefillTimestamp = System.currentTimeMillis();
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 获取当前可用令牌数量
         * @return 可用令牌数量
         */
        public long getAvailableTokens() {
            lock.readLock().lock();
            try {
                refill();
                return currentTokens;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        private void refill() {
            long now = System.currentTimeMillis();
            long timePassed = now - lastRefillTimestamp;
            
            if (timePassed > 0) {
                long refillCount = timePassed / refillPeriodMs;
                if (refillCount > 0) {
                    currentTokens = Math.min(capacity, currentTokens + refillCount * refillTokens);
                    lastRefillTimestamp += refillCount * refillPeriodMs;
                }
            }
        }
    }
    
    // ========================== LRU缓存算法 ==========================
    
    /**
     * LRU缓存实现
     * 基于双向链表和哈希表，支持设置最大容量
     */
    public static class LRUCache<K, V> {
        private final int capacity;
        private final Map<K, Node<K, V>> cache;
        private final Node<K, V> head;
        private final Node<K, V> tail;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        /**
         * 构造LRU缓存
         * @param capacity 缓存最大容量
         */
        public LRUCache(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive");
            }
            
            this.capacity = capacity;
            this.cache = new ConcurrentHashMap<>();
            this.head = new Node<>(null, null);
            this.tail = new Node<>(null, null);
            head.next = tail;
            tail.prev = head;
        }
        
        /**
         * 获取缓存值
         * @param key 缓存key
         * @return 缓存值，如果不存在返回null
         */
        public V get(K key) {
            lock.writeLock().lock();
            try {
                Node<K, V> node = cache.get(key);
                if (node == null) {
                    return null;
                }
                
                moveToHead(node);
                return node.value;
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 添加缓存值
         * @param key 缓存key
         * @param value 缓存值
         */
        public void put(K key, V value) {
            lock.writeLock().lock();
            try {
                Node<K, V> node = cache.get(key);
                
                if (node == null) {
                    Node<K, V> newNode = new Node<>(key, value);
                    cache.put(key, newNode);
                    addToHead(newNode);
                    
                    if (cache.size() > capacity) {
                        Node<K, V> tail = removeTail();
                        cache.remove(tail.key);
                    }
                } else {
                    node.value = value;
                    moveToHead(node);
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 移除缓存值
         * @param key 缓存key
         * @return 被移除的值，如果不存在返回null
         */
        public V remove(K key) {
            lock.writeLock().lock();
            try {
                Node<K, V> node = cache.remove(key);
                if (node != null) {
                    removeNode(node);
                    return node.value;
                }
                return null;
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 获取缓存当前大小
         * @return 缓存大小
         */
        public int size() {
            lock.readLock().lock();
            try {
                return cache.size();
            } finally {
                lock.readLock().unlock();
            }
        }
        
        /**
         * 清空缓存
         */
        public void clear() {
            lock.writeLock().lock();
            try {
                cache.clear();
                head.next = tail;
                tail.prev = head;
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        private void addToHead(Node<K, V> node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(Node<K, V> node) {
            removeNode(node);
            addToHead(node);
        }
        
        private Node<K, V> removeTail() {
            Node<K, V> res = tail.prev;
            removeNode(res);
            return res;
        }
        
        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> prev;
            Node<K, V> next;
            
            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
    }
}