package com.company.cases;

import com.company.Case;
import com.company.CaseClassLoader;
import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;

@Performance(count = 1000,group = 30)
public class StringTest extends CaseClassLoader {
    static StringBuilder stringBuilder = new StringBuilder("StringBuiler-");
    static String string = "String-";
    /*
     * String
     */
     @WarmUp(firstCount = 5)
    public void addOfString(){
        string += "add、";
    }

    /*
     * StringBuilder
     */

    @WarmUp(firstCount = 5)
    public void appendOfBuilder(){
        stringBuilder.append("append、");
    }
}

