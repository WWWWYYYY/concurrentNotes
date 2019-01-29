package com.concurrent.test.content;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ArrayBlockingQueueTest {

    public static void main(String[] args) {
        test8();
    }

    /**
     * 线程0true
     * 线程1false
     */
    private static void test8(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
//                try {
                    PrintUtils.log(String.valueOf(arr.offer("")));//队列满了则马上直接返回false
//                    PrintUtils.log(String.valueOf(arr.offer("",1,TimeUnit.SECONDS)));//队列满了则等待1s后返回结果
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }.doWork(2);
    }

    private static void test7(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                try {
//                    arr.poll();//没有元素不等待并返回null
                    arr.poll(1,TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.doWork(1);
    }

    /**
     * Exception in thread "线程0" java.util.NoSuchElementException
     * 	at java.util.AbstractQueue.element(AbstractQueue.java:136)
     * 	at com.concurrent.test.content.ArrayBlockingQueueTest$1.running(ArrayBlockingQueueTest.java:26)
     * 	at com.concurrent.utils.ForThreads$1.run(ForThreads.java:13)
     */
    private static void test6(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {

                PrintUtils.log(arr.element());

            }
        }.doWork(1);
    }
    /**
     * 线程0null
     *return (count == 0) ? null : itemAt(takeIndex);
     */
    private static void test5(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {

                    PrintUtils.log(arr.peek());

            }
        }.doWork(1);
    }

    /**
     *线程0ready take
     */
    private static void test4(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                try {
                    PrintUtils.log("ready take");
                    arr.take();
                    PrintUtils.log("ready success");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.doWork(1);
    }
    /**
     *线程1ready put
     * 线程0ready put
     * 线程1ready success
     */
    private static void test2(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                try {
                    PrintUtils.log("ready put");
                    arr.put("");
                    arr.put("");
                    PrintUtils.log("ready success");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.doWork(2);
    }
    /**
     * Exception in thread "线程0" java.util.NoSuchElementException
     * 	at java.util.AbstractQueue.remove(AbstractQueue.java:117)
     * 	at com.concurrent.test.content.ArrayBlockingQueueTest$1.running(ArrayBlockingQueueTest.java:18)
     * 	at com.concurrent.utils.ForThreads$1.run(ForThreads.java:13)
     */
    private static void test3(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                    arr.remove();

            }
        }.doWork(1);
    }

    /**
     * Exception in thread "线程1" java.lang.IllegalStateException: Queue full
     * 	at java.util.AbstractQueue.add(AbstractQueue.java:98)
     * 	at java.util.concurrent.ArrayBlockingQueue.add(ArrayBlockingQueue.java:283)
     * 	at com.concurrent.test.content.ArrayBlockingQueueTest$1.running(ArrayBlockingQueueTest.java:17)
     * 	at com.concurrent.utils.ForThreads$1.run(ForThreads.java:13)
     */
    private static void test1(){
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                arr.add("");
            }
        }.doWork(2);
    }
}
