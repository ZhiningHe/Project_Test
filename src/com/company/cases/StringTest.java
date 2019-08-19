package com.company.cases;

import com.company.Case;
import com.company.CaseClassLoader;
import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;



public class StringTest extends CaseClassLoader implements Case {
    static StringBuilder stringBuilder = new StringBuilder("StringBuiler-");
    static String string = "String-";
    /**
     * String
     */
    @MarkMethod @WarmUp(firstCount = 5)
    @Performance(count = 1000,group = 30)
    public void addOfString(){
        string += "add、";
    }

    /**
     * StringBuilder
     */
    @MarkMethod
    @Performance(count = 1000,group = 30)
    @WarmUp(firstCount = 5)
    public void appendOfBuilder(){
        stringBuilder.append("append、");
    }
}

