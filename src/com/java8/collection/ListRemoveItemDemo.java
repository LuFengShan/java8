package com.java8.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListRemoveItemDemo {
	private static List<String> list;

	static {
		list = new ArrayList<>(100);
		for (int i = 0; i < 100; i++) {
			list.add("Item" + (i + 1));
		}
	}

	public static void main(String[] args) {
		// first
		removeItemByIterator(list, "Item10");
		// second
		removeItemByCopyOnWriteArrayList(list, "Item99");

	}

	private static void print(List<String> list) {
		list.forEach(System.out::println);
	}

	/**
	 * list通过iterator来正确的删除元素
	 * @param list
	 * @param target
	 */
	public static void removeItemByIterator(List<String> list, String target) {
		Iterator<String> iter = list.iterator();
		String item;
		while (iter.hasNext()) {
			item = iter.next();
			if (Objects.equals(item, target)) {
				iter.remove();
			}
		}
		print(list);
	}

	/**
	 * list通过并发框架中的{@link java.util.concurrent.CopyOnWriteArrayList}来正确的删除元素
	 * @param list
	 * @param target
	 */
	public static void removeItemByCopyOnWriteArrayList(List<String> list, String target) {
		CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList(list);
		for(String item : copyOnWriteArrayList){
			if (Objects.equals(item, target)) {
				copyOnWriteArrayList.remove(item);
			}
		}
		print(copyOnWriteArrayList);
	}

}
