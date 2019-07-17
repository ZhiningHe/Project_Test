package com.company;

import com.company.cases.arraysort;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {

        int a[] = {1,6,4,34,5,5,5,23,4,2,1,2,2,7,8,7,6,5,9,25,26,75,33};
        //Sort s = new Sort();
        //s.Two(a);
        arraysort s = new arraysort();
        //系统排序
        s.systemSort(a);
        //测试类加载实例
        CaseClassLoader loader = new CaseClassLoader();
        //开始加载、测试
        loader.load(s.getClass().getPackage().getName()).startTest();
    }
}
