package com.java8.m20260414;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替换顺序输出字母。基于juc中的独占锁
 */
public class SortPrintUpperLower {
    private Lock lock = new ReentrantLock();
    private Condition upperCondition = lock.newCondition();
    private Condition lowerCondition = lock.newCondition();
    // 定义一个标志，多线程可见性
    private volatile boolean shouldPringUpper = true;

    public static void main(String[] args) {
        System.out.println("hello");
        SortPrintUpperLower sortPrintUpperLower = new SortPrintUpperLower();

        Runnable upperRunnable = () -> {
            for (char c = 'A'; c <= 'Z'; c++) {
                // 互斥性打印字母
                sortPrintUpperLower.printUpper(c);
            }
        };
        Thread upperTherd = new Thread(upperRunnable);
        upperTherd.start();

        Runnable lowerRunnable = () -> {
            for (char c = 'a'; c <= 'z'; c++) {
                // 互斥性打印字母
                sortPrintUpperLower.printLower(c);
            }
        };
        Thread lowerTherd = new Thread(lowerRunnable);
        lowerTherd.start();

        try {

            upperTherd.join();
            lowerTherd.join();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }

    }

    private void printUpper(char c) {
        lock.lock();
        try {
            // 当大写的标志为真时，可以打印，不为真时，当前线程等待
            while (!shouldPringUpper) {
                upperCondition.await(); // 有等待就有唤醒
            }
            System.out.print(c);
            // shouldPringUpper标记为假
            shouldPringUpper = false;
            lowerCondition.signal();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }

    }

    private void printLower(char c) {
        lock.lock();
        try {
            // 当大写的标志为真时，小写线程中断
            while (shouldPringUpper) {
                lowerCondition.await(); // 有等待就有唤醒
            }
            System.out.print(c);
            // shouldPringUpper标记为真
            shouldPringUpper = true;
            upperCondition.signal();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }

    }

}