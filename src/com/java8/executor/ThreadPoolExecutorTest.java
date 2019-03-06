package com.java8.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ThreadPoolExecutor实例
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        // 使用一个工厂方法Executors.newFixedThreadPool(int)来得到一个线程池
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        // ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            TaskRunnable tr = new TaskRunnable("文件名 " + i);
            System.out.println("一个新的文件已经被增加阅读 : " + tr.getFileName());
            // 提交任务到执行器
            threadPoolExecutor.execute(tr);
        }
        threadPoolExecutor.shutdown();
    }
}

/**
 * 启动线程来读取文件
 */
class TaskRunnable implements Runnable {
    private String fileName;

    public TaskRunnable(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            System.out.println("从 " + fileName + " 中获取数据，线程名字 " + Thread.currentThread().getName());
            Thread.sleep(5000); // Reading file
            System.out.println(fileName + " 已经读取完毕，线程名字 " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}