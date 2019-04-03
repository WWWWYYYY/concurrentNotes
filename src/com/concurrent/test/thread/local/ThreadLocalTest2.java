package com.concurrent.test.thread.local;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

/**
 * 让每个线程绑定一个数据库连接就按这个方式。每个线程都会有自己独立的一个连接。
 */
public class ThreadLocalTest2 {

    private static class A{
        //初始化方式1
//        ThreadLocal<String> local =new ThreadLocal<String>(){
//            @Override
//            protected String initialValue() {
//                return Thread.currentThread().getName();
//            }
//        };
        ThreadLocal<String> local =new ThreadLocal<String>();
    }


    public static void main(String[] args) {
        final A a =new A();
        new ForThreads(){
            @Override
            protected void running() {
                //初始化方式2
                a.local.set(Thread.currentThread().getName());
                ThreadSleepTool.sleep(1000);
                PrintUtils.log(a.local.get());//断点使得每个线程都停在这里发现 每个线程下a.local.get()获取的值不一样
            }
        }.doWork(5);

    }
}
