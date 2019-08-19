package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class WriteFile {
    //获取文件输出流
    public static FileWriter setFile(String name) throws IOException {
        String filepath = "D:\\ZhiningHe\\GitHub\\ZhiNing___\\Project_Test\\TestResult\\" + name + "_TestResult.txt";
        File file = new File(filepath);
        //判断文件是不是存在
        if (!file.exists()) {
            file.createNewFile();
        }
        //获取流
        FileWriter writer = new FileWriter(file);
        return writer;
    }
}
