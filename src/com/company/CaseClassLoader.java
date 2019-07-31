package com.company;


import com.company.annotation.MarkMethod;
import com.company.annotation.Performance;
import com.company.annotation.WarmUp;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;


/**
 * 用来加载进行测试的类
 */


class CaseRunner{
    static DecimalFormat df = new DecimalFormat("0.0000");
    private List<Case> list;
    private File file;
    //默认参数
    private static final int DEFAULT_COUNT=100;
    private static final int DEFAULT_GROUP=3;

    public CaseRunner(List<?> caselist,File file) {
        this.list = (List<Case>) caselist;
        this.file = file;
    }


    //获取注解参数
    public void startTest() throws InvocationTargetException, IllegalAccessException, IOException {
        System.out.println();
        System.out.println("正在搜索需要测试的方法……");
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
    //真实测试类
    private void runcase(Case cases, Method method,int count ,int group) throws InvocationTargetException, IllegalAccessException, IOException {
        final int DEFAULT_WARM = 10;
        int warm = DEFAULT_WARM;
        //判断文件是不是存在
        if (!file.exists()){
            file.createNewFile();
        }

        WarmUp warmUp = cases.getClass().getAnnotation(WarmUp.class);
        //如果设置预热，那就读取设置
        if (warmUp != null) {
            warm = warmUp.firstCount();
        }

        System.out.println();
        System.out.println(" ["+cases.getClass().getName()+" 中 "+method.getName()+"方法 ] "
                +"共测试"+group+"组，每组"+count+"次：");
        double sum = 0;
        double[] runtime = new double[group];
        System.out.println("预热……");
        for(int i=0; i<group ;i++) {
            //预热
            System.out.print("group "+(i+1)+": ");
            for (int w =0 ; w<warm; w++ ){
                method.invoke(cases);
            }
            //正式开始
            long start = System.nanoTime();
            for (int j = 0; j < count; j++) {
                method.invoke(cases);
            }
            long end = System.nanoTime();
            runtime[i] = (double)(end-start);
            sum += runtime[i];
            System.out.print("耗时"+(double)(end-start)*0.000001+"ms\r\n");
        }
        System.out.println("\r\n"+"性能报告：");
        System.out.println("1. average："+ 0.000001*(sum/group)+" ms");
        System.out.println("   variance："+variance(runtime,sum/group));
        System.out.println("-------------------------------------------------");
    }
    //计算方差
    private String variance(double[] runtime,double aver) {
        double variance=0.0;
        for (double p : runtime) {
            variance += (p - aver) * (p - aver);
        }
        variance /=runtime.length;
        variance *=0.000001;
        if (variance<aver){
            System.out.println("2. 离散性低，稳定性高");
        }else if(variance > 2*aver){
            System.out.println("2. 离散性很高，稳定性较低");
        }else{
            System.out.println("2. 离散性较高，稳定性偏低");
        }
        return df.format(variance);
    }
}



public class CaseClassLoader {
    //默认包名
    static final String defaultpkg = "com.company.cases";
    List<String> classNamelist = new ArrayList<>();
    //测试类的实例化对象集合
    List<Case> caselist = new ArrayList<>();
    private static File file;

    public CaseRunner load() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return load(defaultpkg);
    }
    public CaseRunner load(String pkg) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //根据这个类找到类加载器加载的某个包下的其他类
        ClassLoader classLoader = this.getClass().getClassLoader();
        String pkg_str = toDir(pkg);
        Enumeration<URL> urls = classLoader.getResources(pkg_str);


        while (urls.hasMoreElements()){
            URL url = urls.nextElement();

            file = new File(String.valueOf(url).substring(6));

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
            //判断这个类是不是Case接口的子类
            if(hasInterface(cls,Case.class)){
                caselist.add((Case) cls.newInstance());
                }
            }

        //找到标识的类
        return new CaseRunner(caselist,file);
    }

    //将包转化为目录形式
    private String toDir(String pkg) {
        return pkg.replace(".","/");
    }
    //判断一个class对象是不是实现了intface接口
    private boolean hasInterface(Class<?> cls, Class<?> intface) {
        Class<?>[] intf =cls.getInterfaces();
        for (Class c:intf
             ) {
            if(c == intface){
                return true;
            }
        }
        return false;
    }

}
