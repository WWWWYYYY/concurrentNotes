package com.concurrent.test.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * 任务类型有两种recursiveTask有结果和recursiveAction无结果
 * 调用方式有两种execute异步和invoke同步
 * 使用线程池机制：ForkJoinPool
 * <p>
 * 并不是所有的服务器都适合，尽量在计算密集型的计算机上多考虑多线程，分而治之fork/join
 * <p>
 * 如果一个大问题可以分解为多个小问题，并且这些小问题还可以继续分解，并且这些大问题和小问题的特性都是一样的，
 *
 * fork/join配合算法可能有不同的效果。
 * 不要把ForkJoinPool
 */
public class ForkJoinTest {

    public static class MyTask extends RecursiveAction {
        private File file;

        public MyTask(File file) {
            this.file = file;
        }

        @Override
        protected void compute() {
            if (file.isDirectory()) {
                File[] fs = file.listFiles();
                List<MyTask> list = new ArrayList<>();
                for (File f : fs) {
                    if (f.isDirectory()) {
                        list.add(new MyTask(f));//创建了多个task不代表要用相应数量的工作线程来工作
                        System.out.println(Thread.currentThread().getName() + f.getAbsolutePath());
                    } else {
                        System.out.println(Thread.currentThread().getName() + f.getAbsolutePath());
                    }
                }
                invokeAll(list);

                //根据需求需要是否让子任务join。
                for (MyTask myTask : invokeAll(list)) {
                    myTask.join();//等待子任务执行完成
                    if (myTask.isCompletedAbnormally()) {//判断是否正常执行，根据业务需要来判断是否需要中断所有任务
                        System.out.println(myTask.getException());
                    }
                }
            } else {
                System.out.println(Thread.currentThread().getName() + file.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        test1();
//        test2();
//        test3();

    }


    /**
     * 例子1：遍历某个文件夹；使用递归的思路。递归让分而治之显得比较明显。但是fork/join能表现的形式不单单是递归
     */
    public static void test1() throws InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(5);
        RecursiveAction action = new MyTask(new File("/Users/mac/Downloads/腾讯课堂视频/（8）高性能Netty框架"));
        pool.execute(action);
        System.out.println("main");
        action.join();//让任务完成后才往下执行，保证任务完全执行。不管子任务是否调用了join，主任务一定要调用join保证整个任务的完整性，子任务调用join可以根据需求的是否需要。
        pool.shutdown();
    }

    //错误例子：ForkJoinPool,可以执行多个不同性质的任务，但是尽量不要这么做，就相当于把ForkJoinPool当做普通线程池来用
    public static void test2() {
        ForkJoinPool pool = new ForkJoinPool(5);
        RecursiveAction action = new RecursiveAction() {
            @Override
            protected void compute() {
                System.out.println("123");
            }

        };
        pool.execute(action);
        RecursiveAction action2 = new RecursiveAction() {
            @Override
            protected void compute() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("123");
                }
            }

        };
        pool.execute(action2);
        System.out.println("main");
        action.join();//让任务完成后才往下执行，保证任务完全执行。不管子任务是否调用了join，主任务一定要调用join保证整个任务的完整性，子任务调用join可以根据需求的是否需要。
        action2.join();
        pool.shutdown();
    }


    public static class MyRecursiveTask extends RecursiveTask{
        private Integer[] arr;
        private int start;
        private int end;
        private int threshold=100;
        public MyRecursiveTask(Integer[] arr,int start,int end){
            this.arr=arr;
            this.start=start;
            this.end=end;
//            System.out.println("start:"+start+",end:"+end);
        }
        @Override
        protected Object compute() {
            if (end-start>threshold){
                int mid =(end+start)/2;
                MyRecursiveTask task1 =new MyRecursiveTask(arr,start,mid);
                MyRecursiveTask task2 =new MyRecursiveTask(arr,mid+1,end);
                invokeAll(task1,task2);
                return (Integer)task1.join()+(Integer)task2.join();
            }else {
                int sum=0;
                for (int i =start;i<=end;i++){
                    sum+=arr[i];
                }
                return sum;
            }

        }
    }
    //求总和 同步调用；二分法，将一个大问题拆分成两个小问题
    public static void test3(){
        int count =10000;
        Integer[] arr = new Integer[count];
        for (int i =1;i<=count;i++){
            arr[i-1]=i;
        }
        ForkJoinPool pool = new ForkJoinPool();
        long start =System.currentTimeMillis();
        MyRecursiveTask task =new MyRecursiveTask(arr,0,arr.length-1);
        pool.invoke(task);
        System.out.println("sum:"+task.join()+",time:"+(System.currentTimeMillis()-start));


        //并不是所有的计算 使用fork/join都会更快些，一些简单的任务或许单线程都更快。
        long start2 =System.currentTimeMillis();
        int s=0;
        for (int i =0;i<count;i++){
            s+=arr[i];
        }
        System.out.println("s:"+s+",time:"+(System.currentTimeMillis()-start2));

    }


}
