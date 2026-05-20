package com.java8.m20260414;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeQuJian {

    public static void main(String[] args) {
        int[][] intervals = {
                { 1, 3 },
                { 2, 6 },
                { 1, 2 },
                { 15, 18 }
        };
        printString(intervals);
        // 
        if (intervals.length == 1) {
            return;
        }
        // 让每一个区间根据第一个首字母排序
        // Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        printString(intervals);
        // 开始排序
        List<int[]> list = new ArrayList<>(intervals.length);
        int[] current = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            int[] next = intervals[i];
            if (current[1] >= next[0]) {
                current[1] = Math.max(current[1], next[1]);
            } else {
                list.add(current);
                current = next;
            }
        }
        list.add(current);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(Arrays.toString(list.get(i)));
        }
    }

    private static void printString(int[][] intervals) {
        for (int i = 0; i < intervals.length; i++) {
            System.out.println(Arrays.toString(intervals[i]));
        }
        System.out.println("-----------");
    }

}
