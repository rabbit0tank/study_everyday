package org.algorithm.leetcode._20241105;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

class Solution {
    public static void main(String[] args) {
//        System.out.println(minimumLength("bacbcbb"));
        int[] nums= new int[]{1,1,1,1,0,0,0,5,4,3,19,17,16,15,15,15,19,19,19,19};
        System.out.println(minChanges_2(nums,20));
    }
    public static int minimumLength(String s) {
        //统计各个字母的个数，如果为2或者1就为最终态，
        //如果>2,如果数字为奇数，那最终会变为1
        //如果为偶数，那最终结果会变成2
        int[] count= new int[26];
        for(char c:s.toCharArray()){
            count[c-'a']+=1;
        }
        AtomicInteger result= new AtomicInteger();

        Arrays.stream(count).forEach((item)->{
            if(item!= 0){
                result.addAndGet(item % 2 == 1 ? 1 : 2);
            }
        });
        return result.get();
    }

    public static int minChanges(int[] nums, int k) {
        // 已知k >= nums[i]，且数组元素只能在[0:k]中替换，因此X只可能是[0:k]中的值
        // 枚举每组元素，可以得出哪些X需要替换一次，哪些X需要替换两次，差分数组计数即可
        int n = nums.length;
        int[] diff = new int[k+2]; // 多出来的容量是为了防止op1+1和op0+1越界
        for(int i = 0;i < n/2;++i){
            int a = nums[i], b = nums[n-1-i];
            int op0 = Math.abs(a-b);
            // X取值为[0:op0)和(op0:op1]时，这组元素只需要操作一次
            int op1 = Math.max(Math.max(a, k-a), Math.max(b, k-b));
            // X取值大于op1时，这组元素需要操作两次
            diff[0]++;
            diff[op0]--;
            diff[op0+1]++;
            diff[op1+1]++;
        }
        int ans = diff[0];
        for(int i = 1;i <= k;++i){
            diff[i] += diff[i-1];
            ans = Math.min(ans, diff[i]);
        }
        return ans;
    }

    public static int minChanges_2(int[]nums,int k){
        int[] countList=new int[k+1];
        int max=0;
        int maxIndex=k;
        int n=nums.length;
        int avg=0;
        for(int i=0;i<n/2;i++){
            int index= Math.abs(nums[i]-nums[n-i-1]);
            avg+=index;
            countList[index]++;
            if(max<countList[index]){
                max=countList[index];
            }
        }
        for(int i=0;i<=k;i++){
            if(countList[i]==max){
                maxIndex=i;
                break;
            }
        }
        int result_1=countResult(nums,k,maxIndex);

        avg=avg/(n/2);
        max=0;
        maxIndex=k;
        for(int i=0;i<=avg;i++){
            if(countList[i]>=max){
                if(countList[i]==max){
                    if(i<maxIndex){
                        maxIndex=i;
                    }
                }else {
                    maxIndex=i;

                }
                max=countList[i];

            }
        }
        int result_2=countResult(nums,k,maxIndex);
        return Math.min(result_1, result_2);
    }

    public static int countResult(int[] nums,int k,int maxIndex){
        int result=0;
        int n=nums.length;

        for(int i=0;i<n/2;i++){
            int index= Math.abs(nums[i]-nums[n-i-1]);
            if(index!=maxIndex){
                if(index>maxIndex){
                    result++;
                }else {
                    if(nums[i]<=nums[n-i-1]){
                        if(k-nums[i]<maxIndex&&nums[n-i-1]<maxIndex){
                            result+=2;
                        }else {
                            result++;
                        }
                    }else {
                        if(k-nums[n-i-1]<maxIndex&&nums[i]<maxIndex){
                            result+=2;
                        }else {
                            result++;
                        }
                    }
                }

            }
        }
        return result;
    }
}
