package com.concurrent.utils;

/**
 * 方便创建一个或多个线程
 */
public abstract class ForThreads {
    public  Thread[] doWork(int count){
        if (count<0) throw new IllegalArgumentException();
        Thread[] threads = new Thread[count];
        for (int i=0;i<count;i++){
            threads[i] =new Thread("线程"+i){
                @Override
                public void run() {
                    running();
                }
            };
            threads[i].start();
        }
        return threads;
    }

    protected abstract  void running();
}
