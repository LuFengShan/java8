package com.java8.mianshi;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MaoPao {
	private static int arr[] = {10, 9, 3, - 1, 20, 15};
	
	@Test
	public void maoPao() {
		System.out.println("排序前:" + Arrays.toString(arr));
		int flag;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					flag = arr[i];
					arr[i] = arr[j];
					arr[j] = flag;
				}
			}
		}
		System.out.println("排序后:" + Arrays.toString(arr));
	}
	
	@Test
	public void testKuaiSuPaiXu() {
		System.out.println("排序前:" + Arrays.toString(arr));
		int i = arr.length / 2;
		System.out.println(i);
		System.out.println("排序后:" + Arrays.toString(arr));
	}
	
	public static void quickSort(int[] its, int left, int right) {
		int l = left;
		int r = right;
		int middle = arr[(left + right) / 2];
		System.out.println(middle);
		while (l < r) {
			// 在中间值的左边一起找，如果找不到则退出
			while (arr[l] < arr[middle]) {
				l++;
			}
			while (arr[r] > arr[middle]) {
				r--;
			}
			// 说明左右已经分完了
			if (l >= r){
				break;
			}
		}
	}
	
	/**
	 * 选择排序,时间复杂度，n2
	 */
	@Test
	public void testXuanZePaiXu() {
		System.out.println("排序前:" + Arrays.toString(arr));
		for (int i = 0; i < arr.length; i++) {
			int minIndex = i;
			int min = arr[i];
			for (int j = i + 1; j < arr.length; j++) {
				if (min > arr[j]) { // 假定的最小值不是最小的
					min = arr[j]; // 重置min
					minIndex = j; // 重置minIndex
				}
			}
			if (minIndex != 0) {
				arr[minIndex] = arr[i];
				arr[i] = min;
			}
		}
		
		System.out.println("排序后:" + Arrays.toString(arr));
	}
	
	/**
	 * 插入排序,时间复杂度，n2
	 */
	@Test
	public void testChaRuPaiXu() {
		System.out.println("排序前:" + Arrays.toString(arr));
		int insertVal;
		int insertIndex;
		for (int i = 1; i < arr.length; i++) {
			insertVal = arr[i];
			insertIndex = i - 1; // 待插入数前面数的坐标
			while (insertIndex >= 0 && insertVal < arr[insertIndex]) {
				arr[insertIndex + 1] = arr[insertIndex];
				insertIndex--;
			}
			arr[insertIndex + 1] = insertVal;
		}
		System.out.println("排序后:" + Arrays.toString(arr));
	}
	
	@Test
	public void testXiErPaiXu() {
		System.out.println("排序前:" + Arrays.toString(arr));
		System.out.println("排序后:" + Arrays.toString(arr));
	}
}
