package com.java8.m20260414;

import java.util.Objects;

public class DaShuXiangJia {
    public static void main(String[] args) {
        String num1 = "1";
        String num2 = "999";
        System.out.println(addString(num1, num2));
    }

    public static String addString(String num1, String num2) {
        if (Objects.isNull(num1)) {
            num1 = "";
        }
        if (Objects.isNull(num2)) {
            num2 = "";
        }

        int i = num1.length() - 1, j = num2.length() - 1, carry = 0;
        StringBuilder sb = new StringBuilder();
        while (i>=0 || j>=0 || carry > 0) {
            int sum = carry;
            if (i >= 0) {
                sum += num1.charAt(i--) - '0';
            }
            if (j >= 0) {
                sum += num2.charAt(j--) - '0';
            }
            sb.append(sum%10);
            carry = sum / 10;
        }
        return sb.reverse().toString();
    }

}
