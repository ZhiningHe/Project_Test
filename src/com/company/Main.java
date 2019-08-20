package com.company;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {

        //测试类加载实例
        CaseClassLoader loader = new CaseClassLoader();
        //开始加载、测试
        loader.load().startTest("Sort5_100");

    }
}
