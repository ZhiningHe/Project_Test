package com.company.cases;

import com.company.Case;
import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;

import java.util.Arrays;

@Performance(count = 100,group = 3)
public class arraysort implements Case {
    @MarkMethod
    @WarmUp(firstCount = 2)
    public void systemSort(int[]arr){
        Arrays.sort(arr);
    }
}
