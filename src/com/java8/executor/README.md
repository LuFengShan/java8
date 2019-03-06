# 并发框架
Java 5引入了名为“Executor frameworks”的新并发API。它简化了多线程应用程序的设计和开发。
它主要由**Executor，ExecutorService接口和ThreadPoolExecutor类**组成，
**ThreadPoolExecutor**它实现了两个接口，即**Executor和ExecutorService**。ThreadPoolExecutor类提供了线程池的实现。
## 为什么我们需要Executor框架？
当我们创建一个简单的多线程应用程序时，我们使用Runnable创建Runnable对象并构造Thread对象，我们需要创建，执行和管理线程。我们可能很难这样做。
Executor Framework为您完成。它负责创建，执行和管理线程，不仅如此，它还提高了应用程序的性能。
当您遵循每线程任务策略时，您为每个任务创建一个新线程，然后如果系统高度过载，您将出现内存不足错误，您的系统将失败。
如果使用ThreadPoolExecutor，则不会为新任务创建线程。一旦线程完成一个任务，您将任务分配给有限数量的线程，它将被赋予另一个任务。
Executor框架的核心接口是Executor。它有一个叫做“执行”的方法。
```java
public interface Executor {
 void execute(Runnable command);
}
```
还有另一个名为ExecutorService的接口，它扩展了Executor接口。它可以被称为Executor，
它提供可以控制终止的方法和可以生成Future以跟踪一个或多个异步任务进度的方法。它有submit，shutdown，shutdownNow等方法。
## ThreadPoolExecutor
ThreadPoolExecutor是ThreadPool的实际实现。它扩展了AbstractThreadPoolExecutor，它实现了ExecutorService接口。
您可以从Executor类的工厂方法创建ThreadPoolExecutor。建议使用一种方法来获取ThreadPoolExecutor的实例。
Executors类中有4个工厂方法，可用于获取ThreadPoolExecutor的实例。我们使用Executors的newFixedThreadPool来获取ThreadPoolExecutor的实例。
例
```java
ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
// ExecutorService executorService = Executors.newFixedThreadPool(5);
```
以下是Executors类中存在的四种工厂方法。
newFixedThreadPool：此方法返回线程池执行程序，其最大大小（假设n个线程）是固定的。如果所有n个线程都忙于执行任务并提交其他任务，那么它们必须在队列中，直到线程可用。
newCachedThreadPool：此方法返回一个无界线程池。它没有最大的大小，但如果它的任务数量较少，那么它将拆除未使用的线程。如果线程未使用1分钟（keepAliveTime），则会将其拆除。
newSingleThreadedExecutor：此方法返回一个保证使用单个线程的执行程序。 
newScheduledThreadPool：此方法返回一个固定大小的线程池，可以调度命令在给定的延迟后运行，或者定期执行。
让我们创建一个ThreadPoolExecutor的基本示例，我们将使用newFixedThreadPool创建一个ThreadPoolExecutor实例。
```java
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
```
### 使用ThreadPoolExecutor的构造函数
如果要自定义ThreadPoolExecutor的创建，也可以使用其构造函数。
```java
 
   public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue workQueue ,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) ;
```
参数说明
* corePoolSize： corePoolSize是要保留在池中的线​​程数，即使它们是空闲的
* MaximumPoolSize：池中允许的最大线程数
* keepAliveTime：当你有比corePoolSize更多的线程可用时，那么keepAliveTime是时间最多的那个线程将在终止之前等待任务。
* unit： time unit用于keepAliveTime 
* workQueue：workQueue是BlockingQueue，它在执行前保存任务。
* threadFactory：用于创建新线程的Factory。
* handler： RejectedExecutionHandler，用于执行块或队列已满的情况。让我们创建一个RejectedExecutionHandler来处理被拒绝的任务。
```java
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
 
public class RejectTaskHandler implements RejectedExecutionHandler{
 
 @Override
 public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
  FetchDataFromFile ffdf=(FetchDataFromFile) r;
  System.out.println("我们不能执行这个任务");  
 }
}
```
### 如何确定线程池的大小：
你不应该硬编码线程池的大小。它应该由配置提供或从Runtime.availableProcessors计算。