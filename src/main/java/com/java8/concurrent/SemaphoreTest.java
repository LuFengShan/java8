package com.java8.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * 并发实用程序-信号量
 */
public class SemaphoreTest {
    public static void main(String[] args) {

    }
}

class Pool {
    /**
     * 最大信号量（许可证）
     */
    private static final int MAX_AVAILABLE = 100;
    /**
     * 使用给定数量的许可和给定的公平设置创建信号量。
     * 使用给定数量的许可和给定的公平设置创建信号量。
     * <p>
     * PARAMS：
     * 许可证permits - 可用的初始许可证数量。 此值可能为负值，在这种情况下，必须在授予任何获取之前发布。
     * 公平fair - 如果此信号量将保证在争用中首先授予许可证，则为真，否则为假
     */
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    public Object getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    public void putItem(Object x) {
        if (markAsUnused(x))
            available.release();
    }

    // 不是特别有效的数据结构; 只是为了演示

    protected String[] items = {"aa", "bb", "cc", "dd"};
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    /**
     * 获取下一个可用项目
     * @return
     */
    protected synchronized Object getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        return null; // not reached
    }

    /**
     * 把信号量标记为未使用
     * @param item
     * @return
     */
    protected synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }
}
