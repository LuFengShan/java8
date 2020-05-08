package com.java8.suanfa;

import org.junit.jupiter.api.Test;

/**
 * 十分查找算法
 * 1. 前提是待查找的数据已经是排序过了
 */
public class BinarySearch {

	@Test
	public void binarySearchNoRecur() {
		int[] arr = {1, 3, 8, 10, 18, 85, 100};
		int index = binarySearchByNoRecur(arr, 3);
		System.out.println("下标 = " + index);

	}

	/**
	 * 十分查找算法非递归的实现
	 *
	 * @param arr    待查找的数组，数组已经是排好序的升序
	 * @param target 需要查找的数
	 * @return 返回对应的下标，-1表示没有找到
	 */
	private int binarySearchByNoRecur(int[] arr, int target) {
		int left = 0;
		int right = arr.length - 1;

		while (left < right) {
			int mid = (left + right) / 2;
			// 如果中间的下标的值就是我们要找的值，直接结束
			if (arr[mid] == target) {
				return mid;
			} else if (arr[mid] > target) {
				// 如果中间的值大于目标值，说明目标值在中间值的左边，需要向左边查找
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return -1;
	}
}
