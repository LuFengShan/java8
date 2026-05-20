package com.java8.m20260414;

import java.util.Objects;

public class SouSuoXuanZhuan {

    public static void main(String[] args) {
        int[] nums = { 4, 5, 6, 7, 0, 1, 2 };
        int target = 0;
        System.out.println(SouSuoXuanZhuan.search(nums, target));
    }

    /**
     * 如果没有找到，则返回-1
     * 基于二分查找，找到中间位置，判断中间位置等不等于目标值，如果等于，则结束
     * 对左区间进行判断，判断左边界是否大于右边界，如果小于，则证明左区间是有序的。在左区间中判断目标值是否在区间内（left<=target<right）; 如果在左区间内，则right调整为mid-1，否则不在左区间，left调整到mid+1
     * 
     * @param nums   数组
     * @param target 目标值
     * @return
     */
    public static int search(int[] nums, int target) {
        // 如果数组为空或目标值为空，则直接返回-1
        if (Objects.isNull(nums) || nums.length == 0 || Objects.isNull(target)) {
            return -1;
        }
        int left = 0, right = nums.length -1;
        while(left <= right) {
            int mid = left + (right - left) / 2;
            // 判断mid处是不是直接等于目标值
            if (Objects.equals(nums[mid], target)) {
                return mid;
            }
            // 判断左区间是不是有序的
            if (nums[left] < nums[mid]) { // 左区间有序列
                // 判断目标值是不是在左区间内，如果在，缩小右边界
                if (target>=nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else { // 目标值不在左区间内，增加左边界
                    left = mid + 1;
                }
            } else { // 左区间没有序，说明右区间是有序的
                // 如果目标值在右区间中，增加左边界
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else { // 目标值不在右区间内，则缩小右边界
                    right = mid - 1;
                }
            }
        }
        // 如果没有找到值，则返回-1
        return -1;
    }

}
