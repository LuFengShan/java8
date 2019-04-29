package com.java8.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试{@link java.util.concurrent.Executors#newFixedThreadPool(int)}方法的使用
 * 我们使用了新的newFixedThreadPool，所以当我们提交了6个任务时，将创建3个新线程并执行3个任务。
 * 其他3个任务将在等待队列中等待。一旦线程完成任何任务，该线程将挑选另一个任务并执行它。
 */
public class Method_newFixedThreadPool {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 6; i++) {
            TestNewFixedThreadPool loopTask = new TestNewFixedThreadPool("LoopTask " + i);
            service.submit(loopTask);
        }
        service.shutdown();

    }
}


class TestNewFixedThreadPool implements Runnable {

    private String loopTaskName;

    public TestNewFixedThreadPool(String loopTaskName) {
        super();
        this.loopTaskName = loopTaskName;
    }

    @Override
    public void run() {
        System.out.println("开始循环任务名称：" + loopTaskName);
        for (int i = 1; i <= 10; i++) {
            System.out.println("执行任务名称 " + loopTaskName + " ，使用的线程是 " + Thread.currentThread().getName() + "====" + i);
        }
        System.out.println("完成任务名称 " + loopTaskName);
    }
}