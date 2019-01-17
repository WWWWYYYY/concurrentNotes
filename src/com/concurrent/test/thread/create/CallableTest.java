package com.concurrent.test.thread.create;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                return "sasa";
            }
        });
        Thread t = new Thread(task);
        t.start();
        System.out.println("在异步之前打印*****");
        //不要太着急get，get方法是阻塞式方法。可以先做其他事情，在必要的时候get
        System.out.println(task.get());
        task.isCancelled();
        task.isDone();
    }
}
