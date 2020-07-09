package com.java8.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class MonoFluxTest {
	
	private Flux<Integer> generateFluxFrom1To6() {
		return Flux.just(1, 2, 3, 4, 5, 6);
	}
	
	private Mono<Integer> generateMonoWithError() {
		return Mono.error(new Exception("some error"));
	}
	
	@Test
	public void testViaStepVerifier() {
		StepVerifier.create(generateFluxFrom1To6())
				.expectNext(1, 2, 3, 4, 5, 6)
				.expectComplete()
				.verify();
		StepVerifier.create(generateMonoWithError())
				.expectErrorMessage("some error")
				.verify();
		
		StepVerifier.create(Flux.range(1, 6)    // 1
				.map(i -> i * i))   // 2
				.expectNext(1, 4, 9, 16, 25, 36)    //3
				.expectComplete();  // 4
		
		StepVerifier.create(
				Flux.just("flux", "mono")
						.flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
								.delayElements(Duration.ofMillis(100))) // 2
						.doOnNext(System.out::print)) // 3
				.expectNextCount(8) // 4
				.verifyComplete();
		
		StepVerifier.create(
				Flux.range(1, 7)
						.filter(i -> i % 2 == 1)
						.map(i -> i * 10)
		)
				.expectNext(10, 30, 50, 70)
				.verifyComplete();
		
		
	}
	
	private Flux<String> getZipDescFlux() {
		String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
		String[] split = desc.split("\\s+");
		for (String str : split)
			System.out.println(str);
		return Flux.fromArray(desc.split("\\s+"));  // 1
	}
	
	@Test
	public void testSimpleOperators() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);  // 2
		Flux.zip(
				getZipDescFlux(),
				Flux.interval(Duration.ofMillis(200)))  // 3
				.subscribe(t -> System.out.println(t.getT1()),
						null, countDownLatch::countDown
				);    // 4
		countDownLatch.await(10, TimeUnit.SECONDS);     // 5
	}
	
	private String getStringSync() {
		try {
			TimeUnit.SECONDS.sleep(8);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Hello, Reactor!";
	}
	
	@Test
	public void testSyncToAsync() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Mono.fromCallable(() -> getStringSync())    // 1 使用fromCallable声明一个基于Callable的Mono；
				.subscribeOn(Schedulers.elastic())  // 2 使用subscribeOn将任务调度到Schedulers内置的弹性线程池执行，弹性线程池会为Callable的执行任务分配一个单独的线程
				.subscribe(System.out::println, null, countDownLatch::countDown);
		countDownLatch.await(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void testErrorHandling() {
		Flux.range(1, 6)
				.log()
				.map(i -> 10 / (i - 3)) // 当i为3时会导致异常。
				.map(i -> i * i)
				.subscribe(System.out::println, System.err::println);
		// 1. 捕获并返回一个静态的缺省值
		Flux.range(1, 6)
				.log()
				.map(i -> 10 / (i - 3))
				.onErrorReturn(0)   // 1 当发生异常时提供一个缺省值0
				.map(i -> i * i)
				.subscribe(System.out::println, System.err::println);
		// 2. 捕获并执行一个异常处理方法或计算一个候补值来顶替
		Flux.range(1, 6)
				.log()
				.map(i -> 10 / (i - 3))
				.onErrorResume(e -> Mono.just(new Random().nextInt(6))) // 提供新的数据流
				.map(i -> i * i)
				.subscribe(System.out::println, System.err::println);
//		Flux.just(endpoint1, endpoint2)
//				.flatMap(k -> callExternalService(k))   // 1 调用外部服务；
//				.onErrorResume(e -> getFromCache(k));   // 2 如果外部服务异常，则从缓存中取值代替。

//		3. 捕获，并再包装为某一个业务相关的异常，然后再抛出业务异常
//		Flux.just("timeout1")
//				.flatMap(k -> callExternalService(k))   // 1 调用外部服务；
//				.onErrorMap(original -> new BusinessException("SLA exceeded", original)); // 2 如果外部服务异常，将其包装为业务相关的异常后再次抛出。
//		3.1 这一功能其实也可以用onErrorResume实现，略麻烦一点：
//		Flux.just("timeout1")
//				.flatMap(k -> callExternalService(k))
//				.onErrorResume(original -> Flux.error(
//						new BusinessException("SLA exceeded", original)
//				);

//		4. 捕获，记录错误日志，然后继续抛出
//
//		如果对于错误你只是想在不改变它的情况下做出响应（如记录日志），并让错误继续传递下去， 那么可以用doOnError 方法。前面提到，形如doOnXxx是只读的，对数据流不会造成影响：
//		Flux.just(endpoint1, endpoint2)
//				.flatMap(k -> callExternalService(k))
//				.doOnError(e -> {   // 1 只读地拿到错误信息，错误信号会继续向下游传递；
//					log("uh oh, falling back, service failed for key " + k);    // 2 记录日志
//				})
//				.onErrorResume(e -> getFromCache(k));
//		 5. 使用 finally 来清理资源，或使用 Java 7 引入的 “try-with-resource”
//		Flux.using(
//				() -> getResource(),    // 1 第一个参数获取资源；
//				resource -> Flux.just(resource.getAll()),   // 2 第二个参数利用资源生成数据流；
//				MyResource::clean   // 3 第三个参数最终清理资源。
//		);


//		另一方面， doFinally在序列终止（无论是 onComplete、onError还是取消）的时候被执行， 并且能够判断是什么类型的终止事件（完成、错误还是取消），以便进行针对性的清理。如：
		LongAdder statsCancel = new LongAdder();    // 1 用LongAdder进行统计；
		
		Flux<String> flux =
				Flux.just("foo", "bar")
						.doFinally(type -> {
							if (type == SignalType.CANCEL)  // 2 doFinally用SignalType检查了终止信号的类型；
								statsCancel.increment();  // 3 如果是取消，那么统计数据自增；
						})
						.take(1);   // 4 take(1)能够在发出1个元素后取消流。

//		重试
//		还有一个用于错误处理的操作符你可能会用到，就是retry，见文知意，用它可以对出现错误的序列进行重试。
//		请注意：**retry对于上游Flux是采取的重订阅（re-subscribing）的方式，因此重试之后实际上已经一个不同的序列了， 发出错误信号的序列仍然是终止了的。举例如下：
		Flux.range(1, 6)
				.map(i -> 10 / (i - 3))
				.log()
				.retry(1)
				.subscribe(System.out::println, System.err::println);
	}
	
	@Test
	public void testMono() {
		Mono.just(10)
				.log()
				.then(Mono.just(20))
				.log()
				.subscribe(System.out::println);
		
		Mono<Integer> single = Flux.range(1, 6)
				.filter(integer -> integer > 10)
				.log()
				.switchIfEmpty(Mono.just(0))
				.single();
		single.subscribe(System.out::println);
	}
	
	
}
