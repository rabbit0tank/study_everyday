package org.algorithm.leetcode._20241108;

public class Solution {
    public static void main(String[] args) {
        int[][]grid = {{0,1,0},
                        {1,0,1}};
        System.out.println(minimumArea(grid));
    }
public static int minimumArea(int[][] grid) {
    //找到所有1的位置，
    //将在x轴上最长的距离记下
    //将在y轴上最长的距离记录下

    boolean findMinOne=false;
    int maxOfX=0;
    int maxOfY=0;
    int min=Integer.MAX_VALUE;
    int n=grid.length;
    int minOfX=n;
    int minOfY=n;
    for(int i=0;i<n;i++){

        for(int j=0;j<grid[i].length;j++){
            if(grid[i][j]==1){

                if(minOfX>j){

                    minOfX=j;
                }
                if(minOfY>i){
                    minOfY=i;
                }
                if(maxOfX<j){
                    maxOfX=j;
                }
                maxOfY=i;

            }
        }
    }

    return (maxOfX+1-minOfX)*(maxOfY+1-minOfY);
}
}
