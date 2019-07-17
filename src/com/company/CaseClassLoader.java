package com.company;


import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import com.sun.xml.internal.ws.server.ServerRtException;
import sun.dc.pr.PRError;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 用来加载进行测试的类
 */


class CaseRunner{
    private List<Case> list;
    //默认参数
    private static final int DEFAULT_COUNT=100;
    private static final int DEFAULT_GROUP=3;
    public CaseRunner(List<?> caselist) {
        this.list = (List<Case>) caselist;
    }

    public void startTest() throws InvocationTargetException, IllegalAccessException {
        for (Case cases : list){
            //---1.先设置为默认的参数
            int count = DEFAULT_COUNT;
            int group = DEFAULT_GROUP;

            //---2.获取类的配置
            Performance performance = cases.getClass().getAnnotation(Performance.class);
            if (performance!=null){
                count = performance.count();
                group = performance.group();
            }

            //寻找需要测试的方法
            Method[] methods = cases.getClass().getMethods();
            //寻找被Mark标记的方法
            for (Method method : methods){
                //取得注解的MarkMethod
                MarkMethod markMethod = method.getAnnotation(MarkMethod.class);
                if(markMethod == null){
                    continue;
                }

                //---3.设置方法的配置
                Performance per = method.getAnnotation(Performance.class);
                if (per!=null){
                    count = per.count();
                    group = per.group();
                }

                //开始执行方法进行测试
                runcase(cases,method,count,group);
            }
        }
    }
    private void runcase(Case cases, Method method,int count ,int group) throws InvocationTargetException, IllegalAccessException {
        final int DEFAULT_WARM = 10;
        int warm = DEFAULT_WARM;
        WarmUp warmUp = cases.getClass().getAnnotation(WarmUp.class);
        //如果设置预热，那就读取设置
        if (warmUp != null) {
            warm = warmUp.firstCount();
        }

        for(int i=0; i<group ;i++) {
            //预热
            System.out.println("group "+i+":");
            for (int w =0 ; w<warm; w++ ){
                method.invoke(cases);
            }
            //正式开始
            long start = System.nanoTime();
            for (int j = 0; j < count; j++) {
                method.invoke(cases);
            }
            long end = System.nanoTime();
            System.out.print(" "+(double)(end-start)+"ns");
        }
    }
}



public class CaseClassLoader {
    //默认包名
    static final String defaultpkg = "com.company.cases";
    List<String> classNamelist = new ArrayList<>();
    //测试类的实例化对象集合
    List<Case> caselist = new ArrayList<>();

    public CaseRunner load() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return load(defaultpkg);
    }
    public CaseRunner load(String pkg) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //根据这个类找到类加载器加载的某个包下的其他类
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<URL> urls = classLoader.getResources(pkg);


        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            //如果不是一个.class文件就跳过
            if (!url.getProtocol().equals("file")){
                continue;
            }
            //对文件名进行解码
            String dirname = URLDecoder.decode(url.getPath(),"UTF-8");
            File dir = new File(dirname);
            //不是一个文件夹
            if (!dir.isDirectory()){
                continue;
            }

            //找到是.class文件也是一个文件夹的文件路径
            //扫描所有的文件
            File[] files = dir.listFiles();
            if(files == null){
                continue;
            }

            for (File f:files){
                if (f.getName().endsWith(".class")){
                    String filename = f.getName();
                    String className = filename.substring(0,filename.length()-6);
                    classNamelist.add(className);
                }
            }
        }

        for (String classname:classNamelist
             ) {
            //获得这个类的class对象
            Class cls = Class.forName(pkg+"."+classname);
            //判断这个类是不是case接口的子类
            if(hasInterface(cls,Case.class)){
                caselist.add((Case) cls.newInstance());
                }
            }

        //找到标识的类
        return new CaseRunner(caselist);
    }

    //判断一个class对象是不是实现了intface接口
    private boolean hasInterface(Class<?> cls, Class<?> intface) {
        Class<?>[] intf =cls.getInterfaces();
        for (Class c:intf
             ) {
            if(c == cls){
                return true;
            }
        }
        return false;
    }
}
