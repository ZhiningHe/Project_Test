package com.company.cases;

import com.company.Case;
import com.company.CaseClassLoader;
import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;

import java.util.Arrays;


@Performance(count = 2000,group = 10)
public class Sort extends CaseClassLoader implements Case {
    static StringBuilder stringBuilder = new StringBuilder("StringBuiler-");
    static String string = "String-";
    /**
     * String
     */
    @MarkMethod @WarmUp(firstCount = 5)
    @Performance(count = 1000,group = 10)
    public void addOfString(){
        string += "add、";
    }

    /**
     * StringBuilder
     */
    @MarkMethod
    @Performance(count = 1000,group = 10)
    @WarmUp(firstCount = 5)
    public void appendOfBuilder(){
        stringBuilder.append("append、");
    }
}

