package com.concurrent.test.pool;

import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorsTest {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test3();
    }


    /**
     *
     * invokeAny 调用时主线程阻塞，当发现有一个任务正常执行完成并返回后，其他任务将被中断。如果有更早的任务发生了异常则不会再被中断。
     * main------1548562711864
     * main------1007
     * main------789
     * java.lang.InterruptedException: sleep interrupted
     * 	at java.lang.Thread.sleep(Native Method)
     * 	at com.concurrent.utils.ThreadSleepTool.sleep(ThreadSleepTool.java:7)
     * 	at com.concurrent.test.pool.ExecutorsTest$2.call(ExecutorsTest.java:30)
     * 	at com.concurrent.test.pool.ExecutorsTest$2.call(ExecutorsTest.java:27)
     * 	at java.util.concurrent.FutureTask.run(FutureTask.java:262)
     * 	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:471)
     * 	at java.util.concurrent.FutureTask.run(FutureTask.java:262)
     * 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
     * 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
     * 	at java.lang.Thread.run(Thread.java:745)
     * java.lang.InterruptedException: sleep interrupted
     * 	at java.lang.Thread.sleep(Native Method)
     * 	at com.concurrent.utils.ThreadSleepTool.sleep(ThreadSleepTool.java:7)
     * 	at com.concurrent.test.pool.ExecutorsTest$1.call(ExecutorsTest.java:23)
     * 	at com.concurrent.test.pool.ExecutorsTest$1.call(ExecutorsTest.java:20)
     * 	at java.util.concurrent.FutureTask.run(FutureTask.java:262)
     * 	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:471)
     * 	at java.util.concurrent.FutureTask.run(FutureTask.java:262)
     * 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
     * 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
     * 	at java.lang.Thread.run(Thread.java:745)
     */
    private static void test5(){
        ExecutorService pool = Executors.newFixedThreadPool(5);//

        Callable<String> tk_1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadSleepTool.sleep(5000);
                return "456";
            }
        };
        Callable<String> tk_2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadSleepTool.sleep(500);
                throw  new Exception();
            }
        };
        Callable<String> tk_3 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadSleepTool.sleep(1000);
                return "789";
            }
        };
        List<Callable<String>> list =new ArrayList<>();
        list.add(tk_1);
        list.add(tk_2);//Exception
        list.add(tk_3);

        try {
            long start =System.currentTimeMillis();
            PrintUtils.log(String.valueOf(start));
            String s =pool.invokeAny(list);
            PrintUtils.log(String.valueOf((System.currentTimeMillis()-start)));
            PrintUtils.log(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * invokeAll，，调用该方法时主线程会阻塞，表示批量任务会统一返回，如果批量任务中有一个任务执行失败，将终止这次批量任务，不影响线程池的继续使用。
     */
    private static void test4() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);//

        Callable<String> tk_1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadSleepTool.sleep(5000);
                return "456";
            }
        };
        Callable<String> tk_2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadSleepTool.sleep(2000);
                throw new Exception();
            }
        };
        Callable<String> tk_3 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadSleepTool.sleep(5000);
                return "789";
            }
        };
        List<Callable<String>> list =new ArrayList<>();
        list.add(tk_1);
        list.add(tk_2);//Exception
        list.add(tk_3);

        try {
            long start =System.currentTimeMillis();
            PrintUtils.log(String.valueOf(start));
            //由于tk_2抛出异常，终止了这次批量任务，但是不影响后续继续使用该线程池，
            List<Future<String>> fs=pool.invokeAll(list);
            PrintUtils.log(String.valueOf((System.currentTimeMillis()-start)));
            for (Future<String> f : fs) {
                PrintUtils.log(f.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Future<String> f =pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadSleepTool.sleep(5000);
                return "999";
            }
        });
        PrintUtils.log(f.get());

    }

    /**
     * 提交任务都是异步完成的（异步的概念就是将任务交给其他线程去做，主线程做后续的事情）
     * execute 无返回值
     * submit 有返回值，submit提交任务是不管是Runnable还是Callable类型，都会有返回值，只是Runnable会返回null，Callable会返回相应的类型
     *
     *
     * shutdown();//不会终止正在执行的任务
     * shutdownNow();//会终止正在执行的任务
     * awaitTermination用于判断 shutdown后线程池是否完全关闭
     */
    private static void test3() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);//
        Future future =pool.submit(new Runnable() {//submit提交任务是不管是Runnable还是Callable类型，都会有返回值，只是Runnable会返回null，Callable会返回相应的类型
            @Override
            public void run() {
                ThreadSleepTool.sleep(4000);
                PrintUtils.printCurrentThreadEnd();
            }
        });

        PrintUtils.log(String.valueOf(future.get()));//调用get方法阻塞，Runnable任务类型返回null
        PrintUtils.printCurrentThreadEnd();
//
//        Callable<String> callable = new Callable<String>() {
//            public String call() throws Exception {
//                System.out.println("This is ThreadPoolExetor#submit(Callable<T> task) method.");
//                return "result";
//            }
//        };
//
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        Future<String> future = executor.submit(callable);
//        System.out.println(future.get());

        pool.shutdown();
//awaitTermination用于判断 shutdown后线程池是否完全关闭
        while (!pool.awaitTermination(1,TimeUnit.SECONDS)){
            PrintUtils.log("pool has not close"+pool.isTerminated());
        }
        PrintUtils.log("pool has  close"+pool.isTerminated());
    }
    /**
     *         new ThreadPoolExecutor(5, 5,
     *                 0L, TimeUnit.MILLISECONDS,
     *                 new LinkedBlockingQueue<Runnable>());
     */
    private static void test2(){
        ExecutorService pool = Executors.newFixedThreadPool(5);//
        pool.execute(new Runnable() {
            @Override
            public void run() {
                ThreadSleepTool.sleep(1000);
                PrintUtils.printCurrentThreadEnd();
            }
        });
        PrintUtils.printCurrentThreadEnd();
//        pool.shutdown();//不会终止正在执行的任务

        /*
        * java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at com.concurrent.utils.ThreadSleepTool.sleep(ThreadSleepTool.java:7)
	at com.concurrent.test.pool.ExecutorsTest$1.run(ExecutorsTest.java:25)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
        *
        * */
        pool.shutdownNow();//会终止正在执行的任务

    }
    /**
     * Executors线程池创建的原语句，以及作用、缺点
     * 一般情况不直接使用Executors创建线程池，而是实例化ThreadPoolExecutor或者ScheduledThreadPoolExecutor
     * 根据需求的性质决定线程数大小、使用的队列类型以及队列大小、以及定制饱和策略
     */
    private static void test1(){
        //创建固定数量的线程，但是队列是无界的，如果某个时段任务太多可能导致OOM
        ExecutorService pool = Executors.newFixedThreadPool(5);
        //new ThreadPoolExecutor(nThreads, nThreads,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());


        //单线程能够保证执行顺序按照队列里的任务顺序执行，但是队列是无界的，如果某个时段任务太多可能导致OOM
        ExecutorService pool_2 = Executors.newSingleThreadExecutor();
        //new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));

        //线程数是Integer.MAX_VALUE（20亿），SynchronousQueue是不存元素的队列；如果某个时段任务过段导致线程数骤增。导致计算机资源不够用，严重影响运行
        ExecutorService pool_3 =Executors.newCachedThreadPool();
        //new ThreadPoolExecutor(0, Integer.MAX_VALUE,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

        //核心线程数为5.最大核心数为Integer.MAX_VALUE（20亿），以及无界队列
        ExecutorService pool_4 =Executors.newScheduledThreadPool(5);
        //new ScheduledThreadPoolExecutor(corePoolSize);


        pool.shutdown();
        pool_2.shutdown();
        pool_3.shutdown();
        pool_4.shutdown();
    }

}
