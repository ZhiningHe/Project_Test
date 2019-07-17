package com.company.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 这个注解可以给用户提供参数的设定
 */

//运行时期注解
@Retention(RetentionPolicy.RUNTIME)
//这个注解可以修饰类和方法
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Performance {
    //每组几次
    int count();
    //有几组
    int group();
}
