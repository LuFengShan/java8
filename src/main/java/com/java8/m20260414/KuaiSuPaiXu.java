package com.java8.m20260414;

import java.util.Arrays;

/**
 * 快速排序，核心是选择一个值作为标准，让标准值左边的元素<标准值<右边元素
 */
public class KuaiSuPaiXu {
    public static void main(String[] args) {
        int[] num = { 4, 6, 7, 9, 5, 10, 3, 11, 8 };
        // 打印原始数组
        System.out.println(Arrays.toString(num));
        quickSort(num, 0, num.length - 1);
        System.out.println(Arrays.toString(num));
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (arr == null || low >= high)
            return;
        // 分区：获取基准值的正确位置
        int pivotIndex = handle(low, high, arr);
        // 递归排序左半部分
        quickSort(arr, low, pivotIndex - 1);
        // 递归排序右半部分
        quickSort(arr, pivotIndex + 1, high);
    }

    private static int handle(int left, int right, int[] arr) {
        int pivot = arr[right];
        int flagIndex = left - 1;
        for (int i = left; i < right; i++) {
            if (arr[i] <= pivot) {
                flagIndex++;
                int temp = arr[flagIndex];
                arr[flagIndex] = arr[i];
                arr[i] = temp;
            }
        }

        flagIndex++;
        int temp = arr[flagIndex];
        arr[flagIndex] = arr[right];
        arr[right] = temp;
        return flagIndex;
    }

}
