package com.java8.suanfa;

public class ArrBinaryTree {
    private int[] arr;

    public ArrBinaryTree(int[] arr) {
        this.arr = arr;
    }

    // 对这个数组按二叉树前序遍历进行排序，假设二叉树是一个完全二叉树
    // 左子树的下标 = 2 * i + 1
    // 右子树下标 = 2 * i + 2
    // 父节点下标 = (i - 1) / 2
    public void preOrder(int index) {
        if (arr == null || arr.length == 0) {
            return;
        }
        System.out.print(arr[index]);
        // 迭代左侧
        int leftIndex = 2 * index + 1;
        if (leftIndex < arr.length) {
            preOrder(leftIndex);
        }
        // 迭代右侧
        int rightIndex = 2 * index + 2;
        if (rightIndex < arr.length) {
            preOrder(rightIndex);
        }
    }

    public void postOrder(int index) {
        if (arr == null || arr.length == 0) {
            return;
        }
        int leftIndex = 2 * index + 1;
        if (leftIndex < arr.length) {
            postOrder(leftIndex);
        }
        int rightIndex = 2 * index + 2;
        if (rightIndex < arr.length) {
            postOrder(rightIndex);
        }
        System.out.print(arr[index]);
    }
}
