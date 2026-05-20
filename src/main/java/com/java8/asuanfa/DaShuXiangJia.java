package com.java8.asuanfa;

import java.util.Objects;

public class DaShuXiangJia {

    public static void main(String[] args) {
        System.out.println(new DaShuXiangJia().xiangJia("9", "20")); // 1900
    }

    public String xiangJia(String num1, String num2) {
        if (Objects.isNull(num1)) {
            num1 = "";
        }
        if (Objects.isNull(num2)) {
            num2 = "";
        }
        StringBuilder sb = new StringBuilder();
        int n1 = num1.length() - 1;
        int n2 = num2.length() - 1;
        int result = 0;
        while (n1 >= 0 || n2 >= 0 || result > 0) {
            if (n1 >= 0) {
                result += (num1.charAt(n1) - '0');
                n1--;
            }
            if (n2 >= 0) {
                result += (num2.charAt(n2) - '0');
                n2--;
            }
            sb.append(result % 10);
            result = result / 10;
        }
        if (result > 0) {
            sb.append(result);
        }

        return sb.reverse().toString();
    }

}
