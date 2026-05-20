package com.java8.m20260414;

import java.util.Arrays;

public class SortM {

    public static void main(String[] args) {
        int[] arr = {34, 7, 23, 32, 5, 62, 31, 12};
        // 测试不同排序算法，注意：数组是引用类型，需每次复制新数组避免干扰
        System.out.println("原数组：" + Arrays.toString(arr));

        int[] arrBubble = Arrays.copyOf(arr, arr.length);
        bubbleSort(arrBubble);
        System.out.println("冒泡排序后：" + Arrays.toString(arrBubble));

        int[] arrSelect = Arrays.copyOf(arr, arr.length);
        selectionSort(arrSelect);
        System.out.println("选择排序后：" + Arrays.toString(arrSelect));

        int[] arrInsert = Arrays.copyOf(arr, arr.length);
        insertionSort(arrInsert);
        System.out.println("插入排序后：" + Arrays.toString(arrInsert));

        int[] arrShell = Arrays.copyOf(arr, arr.length);
        shellSort(arrShell);
        System.out.println("希尔排序后：" + Arrays.toString(arrShell));

        int[] arrQuick = Arrays.copyOf(arr, arr.length);
        quickSort(arrQuick, 0, arrQuick.length - 1);
        System.out.println("快速排序后：" + Arrays.toString(arrQuick));

        int[] arrMerge = Arrays.copyOf(arr, arr.length);
        mergeSort(arrMerge, 0, arrMerge.length - 1);
        System.out.println("归并排序后：" + Arrays.toString(arrMerge));

        int[] arrHeap = Arrays.copyOf(arr, arr.length);
        heapSort(arrHeap);
        System.out.println("堆排序后：" + Arrays.toString(arrHeap));

        int[] arrRadix = Arrays.copyOf(arr, arr.length);
        radixSort(arrRadix);
        System.out.println("基数排序后：" + Arrays.toString(arrRadix));
    }

    // 1. 冒泡排序：相邻元素比较，大的往后"冒泡"
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int n = arr.length;
        // 标记是否发生交换，优化：无交换则已排序完成
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break; // 无交换，提前退出

            }
        }
    }

    // 2. 选择排序：每次选未排序区间的最小元素，放到已排序区间末尾
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            // 找最小元素的索引
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            // 交换当前位置和最小元素位置
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    // 3. 插入排序：将元素逐个插入到已排序区间的正确位置
    public static void insertionSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int n = arr.length;
        // 从第二个元素开始（第一个元素默认已排序）
        for (int i = 1; i < n; i++) {
            int key = arr[i]; // 待插入的元素
            int j = i - 1;
            // 把比key大的元素往后移
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            // 插入key到正确位置
            arr[j + 1] = key;
        }
    }

    // 4. 希尔排序：插入排序的优化，分组插入排序，逐步缩小步长
    public static void shellSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int n = arr.length;
        // 初始步长为n/2，逐步减半
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // 对每个分组进行插入排序
            for (int i = gap; i < n; i++) {
                int key = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > key) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = key;
            }
        }
    }

    // 5. 快速排序：分治思想，选基准值，将数组分为"小于基准"和"大于基准"两部分
    public static void quickSort(int[] arr, int low, int high) {
        if (arr == null || low >= high) {
            return;
        }
        // 分区：获取基准值的正确位置
        int pivotIndex = partition(arr, low, high);
        // 递归排序左半部分
        quickSort(arr, low, pivotIndex - 1);
        // 递归排序右半部分
        quickSort(arr, pivotIndex + 1, high);
    }

    // 快速排序-分区函数：返回基准值的最终位置
    private static int partition(int[] arr, int low, int high) {
        // 选最右侧元素作为基准（也可优化为随机选/三数取中）
        int pivot = arr[high];
        int i = low - 1; // 小于基准区域的右边界
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                // 交换元素，扩大小于基准的区域
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // 将基准值放到正确位置（i+1）
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    // 6. 归并排序：分治思想，先拆分，再合并有序子数组
    public static void mergeSort(int[] arr, int left, int right) {
        if (arr == null || left >= right) {
            return;
        }
        // 中间位置
        int mid = left + (right - left) / 2;
        // 递归拆分左半部分
        mergeSort(arr, left, mid);
        // 递归拆分右半部分
        mergeSort(arr, mid + 1, right);
        // 合并两个有序子数组
        merge(arr, left, mid, right);
    }

    // 归并排序-合并函数
    private static void merge(int[] arr, int left, int mid, int right) {
        // 临时数组，存储合并结果
        int[] temp = new int[right - left + 1];
        int i = left;   // 左子数组指针
        int j = mid + 1;// 右子数组指针
        int k = 0;      // 临时数组指针

        // 合并两个有序子数组
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        // 拷贝左子数组剩余元素
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // 拷贝右子数组剩余元素
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // 将临时数组拷贝回原数组
        System.arraycopy(temp, 0, arr, left, temp.length);
    }

    // 7. 堆排序：利用大顶堆特性，每次取堆顶（最大值）放到数组末尾
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int n = arr.length;

        // 构建大顶堆（从最后一个非叶子节点开始调整）
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // 逐个取出堆顶元素
        for (int i = n - 1; i > 0; i--) {
            // 堆顶（最大值）和当前最后一个元素交换
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // 调整剩余元素为大顶堆
            heapify(arr, i, 0);
        }
    }

    // 堆排序-堆化函数：调整以i为根的子树为大顶堆
    private static void heapify(int[] arr, int n, int i) {
        int largest = i;       // 初始化最大值为根节点
        int left = 2 * i + 1;  // 左子节点
        int right = 2 * i + 2; // 右子节点

        // 左子节点比根节点大
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // 右子节点比最大值大
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // 最大值不是根节点，交换并递归调整
        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(arr, n, largest);
        }
    }

    // 8. 基数排序：按位数从低到高排序（仅适用于非负整数）
    public static void radixSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        // 找到最大值，确定最大位数
        int max = Arrays.stream(arr).max().getAsInt();

        // 按个位、十位、百位...排序
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSort(arr, exp);
        }
    }

    // 基数排序-计数排序（按指定位数）
    private static void countSort(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n]; // 输出数组
        int[] count = new int[10]; // 0-9的计数数组

        // 统计每个位数的出现次数
        for (int i = 0; i < n; i++) {
            count[(arr[i] / exp) % 10]++;
        }

        // 计算前缀和，确定元素在output中的位置
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // 从后往前遍历，保证稳定性
        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        // 拷贝回原数组
        System.arraycopy(output, 0, arr, 0, n);
    }
}
