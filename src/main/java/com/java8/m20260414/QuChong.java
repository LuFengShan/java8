package com.java8.m20260414;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class QuChong {
    public static void main(String[] args) {
        int[] nums = { 30000, 3, 5, 60000, 7, 7 };
        List<Integer> list = Arrays.stream(nums)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        HashSet<Integer> hs = new HashSet<>(list);
        list.clear();
        list.addAll(hs);
        System.out.println(list);
    }
}
