package com.java8.m20260414;

public class TiaoTaiJie {
    public static void main(String[] args) {
        System.out.println(climbStairsDP(20));
    }

    /**
     * 斐波那契
     * 
     * @param n
     * @return
     */
    public static int climbStairsDP(int n) {
        if (n <= 2) {
            // 1个台阶 (1)
            // 2个台阶（1,1），（2）
            return n;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1; // 1个台阶
        dp[2] = 2; // 2个台阶

        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

}
