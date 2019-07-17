package com.company.cases;

import com.company.Case;
import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;

import java.util.Arrays;


@Performance(count = 100,group = 3)
public class Sort implements Case {
    @MarkMethod @WarmUp(firstCount = 2)
    public void Two(int[] arr){
        if(arr.length <=1){
            return;
        }
        TwoQuick(arr,0,arr.length-1);
    }
    private static void TwoQuick(int[] arr,int start, int end){
        if (start>=end){
            return;
        }
        int key = keyOfTwo(arr,start,end);
        TwoQuick(arr,start,key-1);
        TwoQuick(arr,key+1,end);
    }
    private static int keyOfTwo(int []arr, int start, int end){
        int i=start+1,j=end;
        int value = arr[start];
        while (i<=j){
            while (i<end && arr[i]<=value){
                i++;
            }
            while (j>start && arr[j]>=value){
                j--;
            }
            if(i >j) break;
            swap(arr,i,j);i++;j--;
        }
        swap(arr,start,j);
        //j和i交换后，j在i之前，
        // 也就是说j是小于key的区域的最后一个数
        return j;
    }
    private static void swap(int[] array, int i, int j){
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

}