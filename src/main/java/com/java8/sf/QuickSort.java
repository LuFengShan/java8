package com.java8.sf;

import java.util.Arrays;
import java.util.Objects;

public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {1, 5, 3, 7, 9, 2, 4};
        System.out.println(String.format("原始数组：%s", Arrays.toString(arr)));
        quickSort(0, arr.length - 1, arr);
    }

    public static void quickSort(int low, int high, int[] arr) {
        if (Objects.isNull(arr) || low >= high) {
            return;
        }
        int pivotIndex = sortHandle(low, high, arr);
        quickSort(low, pivotIndex - 1, arr);
        quickSort(pivotIndex + 1, high, arr);
    }

    private static int sortHandle(int low, int high, int[] arr) {

        // 选取基准值
        int pivot = arr[high]; // 4
        int i = low - 1; // -1
        // {1, 5, 3, 7, 9, 2, 4}
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) { // 当j等于5时，i还在2的位置，进来后在3的位置，然后进行交换
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
            System.out.println(String.format("i=%d,j=%d,arr=%s", i, j, Arrays.toString(arr)));
        }
        // 最后需要把基准值和中间值交换
        i++;
        int temp = arr[i];
        arr[i] = pivot;
        arr[high] = temp;
        System.out.println(String.format("排序后，i=%d,j=%d,arr=%s", i, high, Arrays.toString(arr)));
        return i;
    }
}
