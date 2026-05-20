package com.java8.string;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * JDK 1.8 特殊字符检查工具类
 */
public class SpecialCharChecker {

    // 定义需要检查的特殊字符正则表达式（字符集内包含所有目标字符）
    // 说明：
    // - \t：制表符，\n：换行符
    // - \u0002：STX 字符，\u0003：ETX 字符，\u0007：BEL 字符（对应 \007）
    // - 其他字符（~ & ' " ;）直接放入字符集，无需转义
    private static final String SPECIAL_CHAR_REGEX = ".*[~&'\";\\t\\n\\u0002\\u0003\\u0007].*";

    // 预编译正则表达式（JDK 1.8 中 Pattern 是线程安全的，预编译可提升性能）
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(SPECIAL_CHAR_REGEX);

    /**
     * 检查输入字符串是否包含目标特殊字符
     *
     * @param input 待检查的字符串（可为 null，返回 false）
     * @return true：包含特殊字符；false：不包含或输入为 null
     */
    public static boolean containsSpecialChar(String input) {
        // 空字符串/null 直接返回 false
        if (input == null || input.isEmpty()) {
            return false;
        }
        // 用预编译的 Pattern 匹配，性能优于每次调用 String.matches()
        return SPECIAL_CHAR_PATTERN.matcher(input).matches();
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        String test1 = "abc~123"; // 包含 ~ → true
        String test2 = "a&b'c\"d;e"; // 包含 & ' " ; → true
        String test3 = "hello\tworld"; // 包含 \t → true
        String test4 = "line1\nline2"; // 包含 \n → true
        String test5 = "test\u0002abc"; // 包含 \u0002 → true
        String test6 = "test\u0003xyz"; // 包含 \u0003 → true
        String test7 = "test\u0007123"; // 包含 \u0007（\007）→ true
        String test8 = "normal string 123"; // 不包含 → false
        String test9 = ""; // 空字符串 → false
        String test10 = null; // null → false

        // 执行测试
        System.out.println(test1 + " → " + containsSpecialChar(test1));
        System.out.println(test2 + " → " + containsSpecialChar(test2));
        System.out.println(test3 + " → " + containsSpecialChar(test3));
        System.out.println(test4 + " → " + containsSpecialChar(test4));
        System.out.println(test5 + " → " + containsSpecialChar(test5));
        System.out.println(test6 + " → " + containsSpecialChar(test6));
        System.out.println(test7 + " → " + containsSpecialChar(test7));
        System.out.println(test8 + " → " + containsSpecialChar(test8));
        System.out.println(test9 + " → " + containsSpecialChar(test9));
        System.out.println(test10 + " → " + containsSpecialChar(test10));
        System.out.println("============");
        test_predicate();
    }


    // 定义需要检查的所有特殊字符（存入 Set 提高查询效率）
    private static final Set<Character> SPECIAL_CHARS = new HashSet<Character>() {{
        add('~');          // 波浪号
        add('&');          // & 符号
        add('\'');         // 单引号
        add('"');          // 双引号
        add(';');          // 分号
        add('\t');         // 制表符
        add('\n');         // 换行符
        add('\u0007');     // 响铃符（对应 \007）
        add('\u0002');     // STX 控制字符
        add('\u0003');     // ETX 控制字符
    }};

    // 核心 Predicate：判断字符串是否包含特殊字符
    public static final Predicate<String> CONTAINS_SPECIAL_CHAR = input -> {
        // 空字符串/null 直接返回 false
        if (input == null || input.isEmpty()) {
            return false;
        }
        // 遍历字符串每个字符，检查是否在特殊字符集合中
        for (char c : input.toCharArray()) {
            if (SPECIAL_CHARS.contains(c)) {
                return true; // 找到任一特殊字符，返回 true
            }
        }
        return false; // 未找到特殊字符，返回 false
    };

    // -------------- 用法示例 --------------
    public static void test_predicate() {
        // 1. 单个字符串校验
        String str1 = "abc~123";
        String str2 = "normal string";
        System.out.println("str1 包含特殊字符：" + CONTAINS_SPECIAL_CHAR.test(str1)); // true
        System.out.println("str2 包含特殊字符：" + CONTAINS_SPECIAL_CHAR.test(str2)); // false

        // 2. 批量字符串校验（结合 Stream API）
        java.util.List<String> strList = java.util.Arrays.asList(
                "a&b'c",    // 包含特殊字符
                "hello\t",  // 包含制表符
                "line1\nline2", // 包含换行符
                "test\u0002",   // 包含 \u0002
                "abc123",   // 不包含
                null,       // null
                ""          // 空字符串
        );

        System.out.println("\n批量校验结果：");
        strList.forEach(str -> {
                    boolean hasSpecialChar = CONTAINS_SPECIAL_CHAR.test(str);
                    System.out.printf("字符串 [%s] → %s%n",
                            str == null ? "null" : str,
                            hasSpecialChar ? "包含特殊字符" : "不包含特殊字符");
                });

        // 3. 过滤包含特殊字符的字符串（Stream 过滤）
        System.out.println("\n包含特殊字符的字符串：");
        strList.stream()
                .filter(CONTAINS_SPECIAL_CHAR) // 直接使用 Predicate 过滤
                .forEach(str -> System.out.println("- " + str));
    }
}
