package com.utils.Publicsecurity;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//定时任务
@Component
@Configurable
@EnableScheduling
public class PrintWordsJob {

    //部署时需要修改ip地址
    static String path = "\\\\10.10.4.31\\exchange\\data";  //共享文件夹的绝对路径
    //static String path = "D:\\data";  //共享文件夹的绝对路径

    static int count = 0;

    @Scheduled(cron = "*/3 * * * * ?")
    public void execute(){
        //System.out.println("---定时任务执行中---");
        File file = new File(path);
        //io操作最好放到一个线程里去
        new Thread(() -> {
            long start = System.currentTimeMillis();
            listAllFiles(file);
            long end = System.currentTimeMillis();
            //System.out.println("共花费：" + (end - start) + "毫秒");
            //System.out.println("有" + count + "个文件");
             count=0;
        }).start();
    }

    public static void listAllFiles(File file) {
        if (file.isFile()) {
            count++;
            String name = file.getName();
            String prefix = name.substring(name.lastIndexOf(".") + 1);
            boolean uncertainty = prefix.equals("tmp"); //判断文件夹中是否有tmp

            if (uncertainty) {
                try {
                    Thread.sleep(1000); // 1秒后在继续执行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String dir = path;
                File files = new File(dir);
                String srcSuffix = "tmp";
                String dstSuffix = "xml";
                List<String> paths = listPath(files, srcSuffix);
                for (String way : paths) {
                    File srcFile = new File(way);
                    String names = srcFile.getName();
                    int idx = names.lastIndexOf(".");
                    String prefixs = names.substring(0, idx);
                    //System.out.println(srcFile.getParent());
                    File dstFile = new File(srcFile.getParent() + "/" + prefixs + "." + dstSuffix);
                    if (dstFile.exists()) {
                        srcFile.delete();
                        continue;
                    }
                    srcFile.renameTo(dstFile);
                }
            }
            //System.out.println("文件夹后缀名:" + prefix);
            //System.out.println("每个文件夹:" + file);
        } else {
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    listAllFiles(file1);
                }
            }
        }
    }
    private static List<String> listPath(File paths, String srcSuffix) {
        List<String> list = new ArrayList<String>();
        File[] files = paths.listFiles();
        Arrays.sort(files);
        for (File file : files) {
            if (file.isDirectory()) {//如果是目录
                //关键是理解以下两步操作(递归判断下级目录)
                List<String> _list = listPath(file, srcSuffix);//递归调用
                list.addAll(_list);//将集合添加到集合中
            } else {//不是目录
                String name = file.getName();
                int idx = name.lastIndexOf(".");
                String suffix = name.substring(idx + 1);
                if (suffix.equals(srcSuffix)) {
                    list.add(file.getAbsolutePath());//把文件的决定路径添加到集合中
                }
            }
        }
        return list;
    }
}
