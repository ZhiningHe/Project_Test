package com.company.cases;

import com.company.Case;
import com.company.CaseClassLoader;
import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;

import java.util.Date;
import java.util.Random;
@Performance(count = 100,group = 10)
public class SortTest extends CaseClassLoader implements Case {
    //获取一个随机的数组
    private static int[] getRandomArray(){
        Date date=new Date();
        Random random = new Random(date.getTime());
        int[] array = new int[10000];
        for(int i=0; i<10000; i++){
            //把数的长度减小
            array[i] = (int) (random.nextInt()*0.00001);
        }
        return array;
    }

    //插入排序
    @MarkMethod @WarmUp(firstCount = 5)
    public static void InserSort(){
        int[] arr = getRandomArray();
        int i,j;
        for (i=1;i<arr.length;i++){
            int tmp = arr[i];j=i-1;
            for (;j>=0;j--){
                if (tmp>=arr[j]){
                    break;
                }else{
                    arr[j+1]=arr[j];
                }
            }
            arr[j+1]=tmp;
        }
    }

    //快速排序
    @MarkMethod @WarmUp(firstCount = 5)
    public static void QuickSort(){
        int[] arr = getRandomArray();
        quick(arr,0,arr.length-1);
    }


    //归并排序
    @MarkMethod @WarmUp(firstCount = 5)
    public static void MergeSort(){
        int[] arr = getRandomArray();
        divide(arr,0,arr.length-1);
    }



    private static void quick(int[] arr,int start, int end){
        if (start>end) return;
        int key = findKey(arr,start,end);
        quick(arr,start,key-1);
        quick(arr,key+1,end);
    }
    private static int findKey(int[] arr, int start, int end) {
        int random = (int)(Math.random()*(end-start+1)+start);
        swap(arr,random,start);
        int value = arr[start];
        int lt = start+1;
        int gt = end;
        while (true){
            while (lt<=end && arr[lt]<=value) lt++;
            while (gt>=start+1 && arr[gt]>=value) gt--;
            if (lt >gt) break;
            swap(arr,lt,gt);
            lt++;gt--;
        }
        swap(arr,gt,start);
        return gt;
    }
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    private static void divide(int[] arr,int start, int end){
        if (start>=end)return;
        int mid = (end+start)/2;
        divide(arr,start,mid);
        divide(arr,mid+1,end);
        merg(arr,start,mid,end);
    }
    private static void merg(int[] arr,int start, int mid,int end){
        int[] newarr = new int[end-start+1];
        int i = start;
        int k=0;
        int j = mid+1;

        while (i<=mid && j<=end){
            if (arr[i]<=arr[j]) newarr[k++]=arr[i++];
            else newarr[k++] = arr[j++];
        }
        int left = i;
        int right = mid;
        if (j<=end){
            left = j;
            right = end;
        }
        while (left<=right){
            newarr[k++] = arr[left++];
        }
        for (int p=0; p<=end-start; p++){
            arr[p+start] = newarr[p];
        }
    }
}
