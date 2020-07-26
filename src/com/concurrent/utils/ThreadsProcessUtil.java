package com.concurrent.utils;

/**
 * 方便创建一个或多个线程
 */
public class ThreadsProcessUtil {



    public static   Thread[] doWork(Runnable runnable ,int count){
        if (count<0) throw new IllegalArgumentException();
        Thread[] threads = new Thread[count];
        for (int i=0;i<count;i++){
            threads[i] =new Thread(runnable,"线程"+i);
            threads[i].start();
        }
        return threads;
    }
}
