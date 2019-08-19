package com.company;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {

        //测试类加载实例
        CaseClassLoader loader = new CaseClassLoader();
        //开始加载、测试
        loader.load().startTest("String30_1000");
    }
}
