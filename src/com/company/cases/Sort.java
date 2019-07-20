package com.company.cases;

import com.company.Case;
import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;

import java.util.Arrays;


@Performance(count = 2000,group = 10)
public class Sort implements Case {

    static int arr[] = {1,6,4,34,5,5,5,23,4,2,1,2,2,7,8,7,6,5,9,25,26,75,33};

    /**
     * 自己写的快速排序
     */
    @MarkMethod @WarmUp(firstCount = 2)
    public void Two(){
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

    /**
     * 系统的排序
     */
    @MarkMethod
    @WarmUp(firstCount = 2)
    public void systemSort(){
        Arrays.sort(arr);
    }
}

