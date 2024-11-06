package org.algorithm.leetcode._20241106;

import java.util.Arrays;

class Solution {
    public static void main(String[] args) {
        int[] nums= new int[]{1,2,3};
        System.out.println(Arrays.toString(resultsArray(nums, 1)));
    }
    public static int[] resultsArray(int[] nums, int k) {
        //先对数组进行处理，如果前者+1=后者，前者对应的值1，
        //先将无能量值的数据算出来，随后依据下标获取有能量值的具体能量值
        int n=nums.length;
        int[] result= new int[n-k+1];
        int[] powerTemp=new int[n-1];
        for(int i=0;i<n-1;i++){
            if(nums[i]+1==nums[i+1]){
                powerTemp[i]+=1;
            }else{
                powerTemp[i]-=1;
            }
        }
        int power=0;
        for(int i =0;i<k-1;i++){
            power+=powerTemp[i];
        }
        if(power==k-1){
            result[0]=nums[k-1];
        }else{
            result[0]=-1;
        }
        for(int i=1;i<n-k+1;i++){
            power-=powerTemp[i-1];
            power+=powerTemp[i+k-2];
            if(power==k-1){
                result[i]=nums[i+k-1];
            }else{
                result[i]=-1;
            }

        }
        return result;
    }
}
