package com.java8.concurrent.section1;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AppleTree {
	
	/**
	 * 树的集合
	 *
	 * @return
	 * @Author
	 * @Param
	 * @Date 2020/1/9
	 * @version V1.1.0
	 **/
	public static AppleTree[] newTreeGarden(int size) {
		AppleTree[] appleTrees = new AppleTree[size];
		for (int i = 0; i < appleTrees.length; i++) {
			appleTrees[i] = new AppleTree("🌳#" + i);
		}
		return appleTrees;
	}
	
	/**
	 * 树标签
	 */
	private final String treeLabel;
	/**
	 * 苹果的个数
	 */
	private final int numberOfApples;
	
	/**
	 * 必须指定树的标签，默认有3个果子
	 *
	 * @return
	 * @Author sgx
	 * @Param
	 * @Date 2020/1/9
	 * @version V1.1.0
	 **/
	public AppleTree(String treeLabel) {
		this.treeLabel = treeLabel;
		numberOfApples = 3;
	}
	
	public int pickApples(String workerName) {
		System.out.printf("森林名称[线程]：%s ，树的标签：%s ，这棵树可摘取的果子数量[picked]：%d 🍏s \n", workerName, treeLabel, numberOfApples);
//		try {
//			//System.out.printf("%s started picking apples from %s \n", workerName, treeLabel);
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return numberOfApples;
	}
	
	/**
	 * 返回森林里面指定树有多少果子
	 *
	 * @return
	 * @Author
	 * @Param
	 * @Date 2020/1/9
	 * @version V1.1.0
	 **/
	public int pickApples() {
		return pickApples(toLabel(Thread.currentThread().getName()));
	}
	
	/**
	 * 树林的名字，这里可以理解为线程的名字
	 *
	 * @return
	 * @Author sgx
	 * @Param
	 * @Date
	 * @version V1.1.0
	 **/
	private String toLabel(String threadName) {
		HashMap<String, String> threadNameToLabel = new HashMap<>();
		threadNameToLabel.put("ForkJoinPool.commonPool-worker-1", "Alice");
		threadNameToLabel.put("ForkJoinPool.commonPool-worker-2", "Bob");
		threadNameToLabel.put("ForkJoinPool.commonPool-worker-3", "Carol");
		threadNameToLabel.put("ForkJoinPool.commonPool-worker-4", "Dan");
		
		// 意思就是当Map集合中有这个key时，就使用这个key值，如果没有就使用默认值defaultValue
		return threadNameToLabel.getOrDefault(threadName, threadName);
	}
	
	public static void main(String[] args) {
		AppleTree tree = new AppleTree("红富士");
		int i = tree.pickApples();
		System.out.println(i);
	}
}
