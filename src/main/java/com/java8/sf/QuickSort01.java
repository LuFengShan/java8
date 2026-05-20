package com.java8.sf;

import java.util.Arrays;
import java.util.Objects;

public class QuickSort01 {

    private static int arr[] = {2, 5, 9, 8, 1, 10, 5};

    public static void main(String[] args) {
        sortQuick(0, arr.length - 1, arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sortQuick(int low, int high, int arr[]) {
        // 如果数组为空，或最小值大于最大索引，则结束
        if (Objects.isNull(arr) || low >= high) return;
        int povitIndex = handle(low, high, arr);
        sortQuick(low, povitIndex - 1, arr);
        sortQuick(povitIndex + 1, high, arr);
    }

    private static int handle(int low, int high, int arr[]) {
        int povit = arr[high]; // expect
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < povit) { // 如果小于则需要替换位置，大值位于最右边，小值位于最左边
                // 记录i的位置
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // 最后要把基准值和i标记的大值替换
        i++;
        arr[high] = arr[i];
        arr[i] = povit;

        return i;
    }
}
