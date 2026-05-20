package com.java8.m20260414;

public class PrintOne {
    public static void main(String[] args) {
        // 打印出只出现一次的数字
        printOneNumber();
    }

    // 打印出只出现一次的数字
    private static void printOneNumber() {
        int[] num = { 2, 3, 2 };
        int result = 0;
        for (int n : num) {
            result ^= n;
        }
        System.out.println(result);
    }

}
