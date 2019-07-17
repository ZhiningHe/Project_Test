package com.company.annotation;


/**
 * 预热
 */


public @interface WarmUp {
    //规定预热次数
    int firstCount() default 5;
}
