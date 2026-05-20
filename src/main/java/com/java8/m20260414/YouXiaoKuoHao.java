package com.java8.m20260414;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * 判断字符串中是否出现成对的括号。基于栈实现，原理是把括号的左半部全部入栈，遇到不是左半部分的话，就出栈一个，对比是不是一对
 */
public class YouXiaoKuoHao {
    public static void main(String[] args) {

        // System.out.println(handle("()"));
        // System.out.println(handle("([]{})"));
        // System.out.println(handle("([])"));
        // System.out.println(handle("([)"));
        // System.out.println(handle("([{])"));
        System.out.println(youxiao("([{}])"));
    }

    public static boolean handle(String s) {

        Stack<Character> stack = new Stack<>();

        Predicate<Character> predicate = c -> Objects.equals(c, '{') || Objects.equals(c, '[')
                || Objects.equals('(', c);
        // 遍历字符串，把字符串转换为字节
        for (char c : s.toCharArray()) {
            if (predicate.test(c)) { // 如果是左半部分就入栈
                stack.push(c);
            } else { // 如果不是右半部分，就进行判断
                // 如果栈现在也为空，则证明是没有括号左半部分数据，则直接返回假
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if (Objects.equals(c, '(') && !Objects.equals(top, ')')) {
                    return false;
                }
                if (Objects.equals(c, '{') && !Objects.equals(top, '}')) {
                    return false;
                }
                if (Objects.equals(c, '[') && !Objects.equals(top, ']')) {
                    return false;
                }

            }

        }
        // 最后，如果栈为空，则证明括号是成对出现的，返回真
        return stack.isEmpty();
    }

    public static boolean core(String str) {
        Map<Character, Character> map = new HashMap<>(8);
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        Deque<Character> stack = new ArrayDeque<>();

        if (Objects.isNull(str) || str.length() == 0) {
            return false;
        }

        for (Character c : str.toCharArray()) {
            if (map.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != map.get(c)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

    public static boolean youxiao(String str) {
        if (Objects.isNull(str) || str.length() == 0) {
            return false;
        }

        Map<Character, Character> map = new HashMap<>();
        map.put('}', '{');
        map.put(']', '[');
        map.put(')', '(');

        char[] array = str.toCharArray();
        Deque<Character> stack = new ArrayDeque<>();
        for (Character character : array) {
            // 如果匹配到最近的右区间，判断左区间在不在
            if (map.containsKey(character)) {
                if (stack.isEmpty() || map.get(character) != stack.pop()) {
                    return false;
                }
            } else {
                stack.push(character);
            }
        }

        return stack.isEmpty();
    }

}
