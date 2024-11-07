package org.algorithm.leetcode._20241107;

/**
 * 给你一个 二进制 字符串 s 和一个整数 k。
 * 如果一个 二进制字符串 满足以下任一条件，则认为该字符串满足 k 约束：
 * - 字符串中 0 的数量最多为 k。
 * - 字符串中 1 的数量最多为 k。
 * 返回一个整数，表示 s 的所有满足 k 约束 的
 * 子字符串
 * 的数量。
 */
public class Solution {
    public static void main(String[] args) {
        System.out.println(countKConstraintSubstrings("110100110",9));
    }
    public static int countKConstraintSubstrings(String s, int k) {
        //滑动窗口暴力解题
        //如果有一次滑动没有更改结果，不用测试之后的，直接输出结果
        int right=1;
        int sizeOfWindow=1;
        int zeroCount=0;
        int oneCount=0;
        int count=0;
        int n=s.length();

        for(int i=0;i<right;i++){
            int value= (int) s.charAt(i);
            if(value==48){
                zeroCount++;
            }else{
                oneCount++;
            }
        }

        while(right!=n){
            int countTemp=count;
            if(zeroCount<=k||oneCount<=k){
                count++;
            }
            int zeroCountTemp=zeroCount;
            int oneCountTemp=oneCount;
            for(int i=right;i<n;i++){
                int rightValue=(int) s.charAt(i);
                int leftValueForOut=(int) s.charAt(i-sizeOfWindow);
                if(rightValue==48){
                    zeroCountTemp++;
                }else{
                    oneCountTemp++;
                }
                if(leftValueForOut==48){
                    zeroCountTemp--;
                }else{
                    oneCountTemp--;
                }
                if(zeroCountTemp<=k||oneCountTemp<=k){
                    count++;
                }
            }

            right++;
            if(right!=n){
                sizeOfWindow++;
                int rightValue=(int) s.charAt(right-1);
                if(rightValue==48){
                    zeroCount++;
                }else{
                    oneCount++;
                }

            }

        }
        int rightValue=(int) s.charAt(right-1);
        if(rightValue==48){
            zeroCount++;
        }else{
            oneCount++;
        }
        if(zeroCount<=k||oneCount<=k){
            count++;
        }
        return count;
    }
}
