package com.java8.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 同步辅助{@link CyclicBarrier}
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        // 创建具有三个线程的CountDownLatch，当所有3方都达到共同障碍点时，将触发CyclicBarrrierFinishEvent
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new CyclicBarrierFinishEvent());
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        // 三个线程
        RunnableTask runnableTask1 = new RunnableTask(cyclicBarrier, 1000L);
        RunnableTask runnableTask2 = new RunnableTask(cyclicBarrier, 2000L);
        RunnableTask runnableTask3 = new RunnableTask(cyclicBarrier, 3000L);
        //创建并启动3个线程
        new Thread(runnableTask1, "Thread-1").start();
        new Thread(runnableTask2, "Thread-2").start();
        new Thread(runnableTask3, "Thread-3").start();

        RunnableTask runnableTask4 = new RunnableTask(cyclicBarrier, 4000L);
        RunnableTask runnableTask5 = new RunnableTask(cyclicBarrier, 5000L);
        RunnableTask runnableTask6 = new RunnableTask(cyclicBarrier, 6000L);

        // 创建并启动3个线程
        new Thread(runnableTask4, "Thread-4").start();
        new Thread(runnableTask5, "Thread-5").start();
        new Thread(runnableTask6, "Thread-6").start();

    }
}

/**
 * 三个线程任务
 */
class RunnableTask implements Runnable {

    private CyclicBarrier cyclicBarrier;
    private Long sleepTime;

    public RunnableTask(CyclicBarrier cyclicBarrier, Long sleepTime) {
        this.cyclicBarrier = cyclicBarrier;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(sleepTime);
            System.out.println(Thread.currentThread().getName() +
                    " 正在等待 " + (cyclicBarrier.getParties() - cyclicBarrier.getNumberWaiting() - 1) +
                    " 其他线程达到共同的障碍点");
            /*
             * 当3个线程都将调用await（）方法（即公共障碍点）时，将触发循环障碍事件并释放所有等待线程。
             */
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("由于" + cyclicBarrier.getParties() + " 线程已达到共同的障碍点 "
                + Thread.currentThread().getName() +
                " 已经被释放");
    }
}

/**
 * 当3个线程（使用CyclicBarrier对象初始化）都到达公共屏障点时，将调用CyclicBarrierFinishEvent
 */
class CyclicBarrierFinishEvent implements Runnable {

    @Override
    public void run() {

        System.out.println("由于三个线程都已经达到了公共的障碍点，CyclicBarrrierFinishEvent已被触发");
        System.out.println("您可以随意更新共享变量");
    }

}