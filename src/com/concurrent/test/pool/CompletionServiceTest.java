package com.concurrent.test.pool;

import com.concurrent.utils.PrintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * CompletionService 让先执行完的任务先返回结果（正在执行的所有任务中），
 *
 * 提高了结果处理效率和cpu的使用率，不会像test2()傻傻的按任务的顺序获取结果，
 */
public class CompletionServiceTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test2();
    }


    private static void test1() throws InterruptedException, ExecutionException {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);


        ThreadPoolExecutor pool = new ThreadPoolExecutor(8, cpu_count, 30, TimeUnit.SECONDS,
                queue, Executors.defaultThreadFactory(), new ThreadPoolExecutorTest.MyReject());

        CompletionService<String> service =new ExecutorCompletionService<String>(pool);


        for (int i =0 ;i<10;i++){
            MyTask.MyCallableTask<String> task =new MyTask.MyCallableTask("task"+(i+1));
            task.isNeedSleep=true;
            task.isRandom=true;
            String str ="";
            service.submit(task);
        }

        for (int i =0 ;i<10;i++) {
            PrintUtils.log(service.take().get());
            PrintUtils.log(pool.toString());

        }

    }

    private static void test2() throws InterruptedException, ExecutionException {
        int cpu_count = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);


        ThreadPoolExecutor pool = new ThreadPoolExecutor(8, cpu_count, 30, TimeUnit.SECONDS,
                queue, Executors.defaultThreadFactory(), new ThreadPoolExecutorTest.MyReject());


        List<Future<String>> list =new ArrayList<>();
        for (int i =0 ;i<10;i++){
            MyTask.MyCallableTask<String> task =new MyTask.MyCallableTask("task"+(i+1));
            task.isNeedSleep=true;
            task.isRandom=true;
            list.add(pool.submit(task));
        }

        for (Future<String> future : list) {
            PrintUtils.log(future.get());
        }

    }
}
