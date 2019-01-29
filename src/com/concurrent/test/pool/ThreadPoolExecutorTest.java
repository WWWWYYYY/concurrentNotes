package com.concurrent.test.pool;

import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
 * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
 * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
 * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
 *
 *
 */
public class ThreadPoolExecutorTest {



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test6();
    }


    /**
     * submit用法和execute用法差不多
     */
    private static void test6() throws ExecutionException, InterruptedException {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);


        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, cpu_count, 30, TimeUnit.SECONDS,
                queue, Executors.defaultThreadFactory(), new MyReject());
        Future<String> future = pool.submit(new MyTask.MyCallableTask<String>("task1"));

        /*
        *
        * 处理了其他事情后get
        * */
        PrintUtils.log(future.get());//获取时阻塞

        PrintUtils.log(pool.toString());
        pool.shutdown();

    }

    /**
     * 提前初始化核心线程prestartAllCoreThreads
     */
    private static void test5() {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);


        ThreadPoolExecutor pool = new ThreadPoolExecutor(6, cpu_count, 30, TimeUnit.SECONDS,
                queue, Executors.defaultThreadFactory(), new MyReject());

        pool.prestartAllCoreThreads();//如果需要，就在执行任务前初始化全部核心线程
        //main------java.util.concurrent.ThreadPoolExecutor@6a50549[Running, pool size = 6, active threads = 0, queued tasks = 0, completed tasks = 0]
        PrintUtils.log(pool.toString());
        pool.shutdown();

    }


    /**
     * MyReject 策略
     */
    private static void test4() {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);


        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, cpu_count, 30, TimeUnit.SECONDS,
                queue, Executors.defaultThreadFactory(), new MyReject());
        PrintUtils.log(String.valueOf("pool.getActiveCount：" + pool.getActiveCount()));

        List<MyTask.MyRunnableTask> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyTask.MyRunnableTask tsk = new MyTask.MyRunnableTask("task" + (i + 1));
            tsk.isNeedSleep = true;
            list.add(tsk);
            pool.execute(tsk);
            PrintUtils.log(String.valueOf("pool.getTaskCount：" + pool.getTaskCount()));
            PrintUtils.log(pool.toString());
        }


        ThreadSleepTool.sleep(1500);
        PrintUtils.log("pool.getCompletedTaskCount：" + String.valueOf(pool.getCompletedTaskCount()));
        pool.shutdown();

        ThreadSleepTool.sleep(15000);
        PrintUtils.log("pool.getCompletedTaskCount：" + String.valueOf(pool.getCompletedTaskCount()));
    }


    /**
     * CallerRunsPolicy 策略
     */
    private static void test3() {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);


        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, cpu_count, 30, TimeUnit.SECONDS,
                queue, Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        PrintUtils.log(String.valueOf("pool.getActiveCount：" + pool.getActiveCount()));

        List<MyTask.MyRunnableTask> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyTask.MyRunnableTask tsk = new MyTask.MyRunnableTask("task" + (i + 1));
            tsk.isNeedSleep = true;
            list.add(tsk);
            pool.execute(tsk);
            PrintUtils.log(String.valueOf("pool.getTaskCount：" + pool.getTaskCount()));
            PrintUtils.log(pool.toString());
        }


        ThreadSleepTool.sleep(1500);
        PrintUtils.log("pool.getCompletedTaskCount：" + String.valueOf(pool.getCompletedTaskCount()));
        pool.shutdown();

        ThreadSleepTool.sleep(15000);
        PrintUtils.log("pool.getCompletedTaskCount：" + String.valueOf(pool.getCompletedTaskCount()));
    }

    /**
     * AbortPolicy策略
     */
    private static void test2() {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);


        //AbortPolicy饱和：线程池达到最大线程数和队列满了，此时抛出异常
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, cpu_count, 30, TimeUnit.SECONDS,
                queue, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        PrintUtils.log(String.valueOf("pool.getActiveCount：" + pool.getActiveCount()));

        List<MyTask.MyRunnableTask> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyTask.MyRunnableTask tsk = new MyTask.MyRunnableTask("task" + (i + 1));
            tsk.isNeedSleep = true;
            list.add(tsk);
            pool.execute(tsk);
            PrintUtils.log(String.valueOf("pool.getTaskCount：" + pool.getTaskCount()));
            PrintUtils.log(pool.toString());
        }
        /**
         * 异常以后主线程挂了
         * main------java.util.concurrent.ThreadPoolExecutor@43b6c732[Running, pool size = 8, active threads = 8, queued tasks = 10, completed tasks = 0]
         * java.util.concurrent.RejectedExecutionException: Task com.concurrent.test.pool.MyTask$MyRunnableTask@56101751 rejected from java.util.concurrent.ThreadPoolExecutor@43b6c732[Running, pool size = 8, active threads = 8, queued tasks = 10, completed tasks = 0]
         * 	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2048)
         * 	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:821)
         * 	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1372)
         * 	at com.concurrent.test.pool.ThreadPoolExecutorTest.test2(ThreadPoolExecutorTest.java:33)
         * 	at com.concurrent.test.pool.ThreadPoolExecutorTest.main(ThreadPoolExecutorTest.java:16)
         */

        ThreadSleepTool.sleep(1500);
        PrintUtils.log("pool.getCompletedTaskCount：" + String.valueOf(pool.getCompletedTaskCount()));
        pool.shutdown();

        ThreadSleepTool.sleep(15000);
        PrintUtils.log("pool.getCompletedTaskCount：" + String.valueOf(pool.getCompletedTaskCount()));
    }

    /**
     * AbortPolicy
     * DiscardPolicy
     * DiscardOldestPolicy
     * CallerRunsPolicy
     * 自定义
     */
    private static void test1() {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        PrintUtils.log(String.valueOf(cpu_count));
        //定义了一个核心线程数为1，最大线程数为10，超过30秒还没有执行任务的的线程将被回收，定义了一个大小的10的阻塞队列
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, cpu_count, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        pool.allowCoreThreadTimeOut(true);//核心线程在allowCoreThreadTimeout被设置为true时会超时退出，默认情况下不会退出。
        pool.prestartAllCoreThreads();//在没有任务的时候预先创建所有核心线程
        pool.prestartCoreThread();//在没有任务的时候预先创建Y一个核心线程
        pool.purge();// 对于Future类的任务，可以获取所有该类已为取消状态的任务
//        pool.awaitTermination()//在调用了shutdown之后判断线程池是否完全终止

    }

    public static class MyReject implements RejectedExecutionHandler {
        /**
         * Creates an {@code AbortPolicy}.
         */
        public MyReject() {
        }

        /**
         * Always throws RejectedExecutionException.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         * @throws RejectedExecutionException always.
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            PrintUtils.log(((MyTask.MyRunnableTask)r).taskName+" has log");
        }
    }
}
