package com.concurrent.utils;

/**
 * 方便创建一个或多个线程
 */
public abstract class ForThreads {
    public  void doWork(int count){
        if (count<0) throw new IllegalArgumentException();
        for (int i=0;i<count;i++){
            Thread t =new Thread("线程"+i){
                @Override
                public void run() {
                    running();
                }
            };
            t.start();
        }
    }

    protected abstract  void running();
}
