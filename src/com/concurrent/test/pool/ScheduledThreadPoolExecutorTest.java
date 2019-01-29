package com.concurrent.test.pool;

import com.concurrent.utils.PrintUtils;

import java.util.concurrent.*;

public class ScheduledThreadPoolExecutorTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test3();
    }


    /**
     * scheduleWithFixedDelay 周期性执行任务
     */
    private static void test3(){
        ScheduledThreadPoolExecutor pool =new ScheduledThreadPoolExecutor(1);
        //创建一个5s后开始执行，之后每秒执行一次的任务
        pool.scheduleWithFixedDelay(new MyTask.MyRunnableTask("task1"), 5,1, TimeUnit.SECONDS);

//        pool.shutdown();
    }
    /**
     * scheduleAtFixedRate 周期性执行任务
     */
    private static void test2(){
        ScheduledThreadPoolExecutor pool =new ScheduledThreadPoolExecutor(1);
        //创建一个5s后开始执行，之后每秒执行一次的任务
        pool.scheduleAtFixedRate(new MyTask.MyRunnableTask("task1"), 5,1, TimeUnit.SECONDS);

//        pool.shutdown();
    }
    /**
     * schedule  只执行一次
     */
    private static void test1() throws ExecutionException, InterruptedException {
        ScheduledExecutorService pool =new ScheduledThreadPoolExecutor(4);
        //创建一个推迟5s后执行的任务，
        ScheduledFuture<String> task1 = pool.schedule(new MyTask.MyCallableTask<String>("task1"), 5, TimeUnit.SECONDS);
        PrintUtils.log(task1.get());

//        pool.scheduleAtFixedRate
//        pool.scheduleWithFixedDelay();
    }
}
