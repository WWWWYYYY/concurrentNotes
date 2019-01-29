package com.concurrent.test.pool;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;

import java.util.concurrent.*;


/**
 * 线程池基本元素
 * 1、线程数组
 * 2、阻塞队列（根据业务需要选择相应类型的阻塞队列）
 *
 * 良好的线程池需要考虑动态的线程数以及任务的管理，以及一些异常情况处理
 */
public class MyThreadPool {
    private TaskType taskType =TaskType.NORMAL;
    private int threadCount =Runtime.getRuntime().availableProcessors()+1;
    enum TaskType {
        DELAY, PRIORITY, NORMAL, DOUBLE
    }

    private int taskCount = 100;

    private BlockingQueue queue;

    private Thread[] threads;

    public MyThreadPool() {
        createBlockingQueue();
        initThreads();
    }

    private void initThreads() {
        threads= new ForThreads(){
            @Override
            protected void running() {
                Runnable r =null;
                Thread t =Thread.currentThread();
                while (!t.isInterrupted()){
                    try {
                        r= (Runnable) queue.take();
                        PrintUtils.log("execute work "+r);
                        r.run();
                        r=null;
                    } catch (InterruptedException e) {
                        PrintUtils.log("被中断");
                        t.interrupt();
                    }catch (Exception e){
                        PrintUtils.log("业务异常进行日志记录");
                    }
                }
            }
        }.doWork(threadCount);
    }

    private void createBlockingQueue() {
        switch (taskType) {
            case DELAY:
                queue = new DelayQueue<>();//11
                break;
            case DOUBLE:
                queue = new LinkedBlockingDeque(taskCount);
                break;
            case NORMAL:
                queue = new ArrayBlockingQueue(taskCount);
                break;
            case PRIORITY:
                queue = new PriorityBlockingQueue(taskCount);
                break;
            default:
                queue = new ArrayBlockingQueue(taskCount);
                break;
        }
    }

    public void execute(MyWork myWork){
//        Object item=null;
//        switch (taskType){
//            case DELAY:
//                break;
//            case DOUBLE:
//                break;
//            case NORMAL:
//                break;
//            case PRIORITY:
//                break;
//            default:
//                break;
//        }
        try {
            queue.put(myWork);
        } catch (InterruptedException e) {//异常情况下要么重试要么日志记录
            PrintUtils.log("提交任务过程中被中断；内容为："+myWork.toString());
        }
    }

    public void destory(){
        for (int i=0;i<threads.length;i++){
            threads[i].interrupt();
            threads[i]=null;
        }
        queue.clear();
    }
    public static class MyWork extends Thread{
        private  String data;

        public  MyWork(String data){
            this.data=data;
        }
        @Override
        public void run() {
            PrintUtils.log(data);
        }

        @Override
        public String toString() {
            return data;
        }

        public void stopWork(){
            Thread.currentThread().isInterrupted();
        }
    }


    private static class Item implements Delayed{
        //单位是秒
        public long t;
        String data;
        public Item(int t,String data){
            this.t=System.nanoTime()+TimeUnit.NANOSECONDS.convert(t,TimeUnit.SECONDS);//将时间最终换算成ns并且加上系统时间的ns
            this.data=data;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.t - System.nanoTime(), unit);
        }

        @Override
        public int compareTo(Delayed o) {//比较时换算成毫秒
            if (this.getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS)>=0) return 1;
            else return -1;
        }

        @Override
        public String toString() {
            return data;
        }
    }
}
