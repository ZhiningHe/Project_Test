package com.company.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 标记需要进行性能测试的方法
 */


//运行时期注解
@Retention(RetentionPolicy.RUNTIME)
//这个注解可以修饰类和方法
@Target(ElementType.METHOD)
public @interface MarkMethod {

}
