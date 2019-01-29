package com.concurrent.test.content;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 由于LinkedBlockingQueue使用了两把重入锁，如果多个线程又有插入又有拿出时效率会比较好,这是由于内部封装了Node对象，是一个链表元素的结构。也因为将每个元素重新包装了Node对象，空间占用比较大，拿具体元素时也会慢一些
 *
 * notFull.await();等待未满条件，当容器放满了元素，线程执行put方法则阻塞，等待未满的情况
 * notEmpty.await();等待未空的条件， 当容器中没有元素，线程调用take时则需要等待，等待容器不是空的情况。
 *
 * 通过test91() test9()比较发现在10w次的拿和放LinkedBlockingQueue没有比ArrayBlockingQueue快多少
 */
public class LinkedBlockingQueueTest {
    private static CountDownLatch countDownLatch =new CountDownLatch(10);
    public static void main(String[] args) {
        test9();
    }


    /**
     * 6700
     */
    private static void test91(){
        long start =System.currentTimeMillis();
        final ArrayBlockingQueue<String> arr = new ArrayBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {

                PrintUtils.log("put start ");
                for (int i=0;i<100000;i++){
                    try {
                        arr.put("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                PrintUtils.log("put end");
                countDownLatch.countDown();
            }
        }.doWork(5);

        new ForThreads(){
            @Override
            protected void running() {
                PrintUtils.log("take start");
                for (int i=0;i<100000;i++){
                    try {
                        arr.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                PrintUtils.log("take end");
                countDownLatch.countDown();
            }
        }.doWork(5);

        try {
            countDownLatch.await();
            PrintUtils.log("time:"+(System.currentTimeMillis()-start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 6200
     */
    private static void test9(){
        long start =System.currentTimeMillis();
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {

                PrintUtils.log("put start ");
                for (int i=0;i<100000;i++){
                    try {
                        arr.put("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                PrintUtils.log("put end");
                countDownLatch.countDown();
            }
        }.doWork(5);

        new ForThreads(){
            @Override
            protected void running() {
                PrintUtils.log("take start");
                for (int i=0;i<100000;i++){
                    try {
                        arr.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                PrintUtils.log("take end");
                countDownLatch.countDown();
            }
        }.doWork(5);

        try {
            countDownLatch.await();
            PrintUtils.log("time:"+(System.currentTimeMillis()-start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Exception in thread "线程0" java.util.NoSuchElementException
     * 	at java.util.AbstractQueue.element(AbstractQueue.java:136)
     * 	at com.concurrent.test.content.LinkedBlockingQueueTest$1.running(LinkedBlockingQueueTest.java:23)
     * 	at com.concurrent.utils.ForThreads$1.run(ForThreads.java:13)
     */
    private static void test8(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {

                PrintUtils.log(arr.element());
            }
        }.doWork(1);
    }
    /**
     * 线程0------null
     */
    private static void test7(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {

                PrintUtils.log(arr.peek());
            }
        }.doWork(1);
    }
    /**
     * Exception in thread "线程0" java.util.NoSuchElementException
     * 	at java.util.AbstractQueue.remove(AbstractQueue.java:117)
     * 	at com.concurrent.test.content.LinkedBlockingQueueTest$1.running(LinkedBlockingQueueTest.java:22)
     * 	at com.concurrent.utils.ForThreads$1.run(ForThreads.java:13)
     */
    private static void test6(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                arr.remove();
            }
        }.doWork(1);
    }
    /**
     * Exception in thread "线程0" java.lang.IllegalStateException: Queue full
     * 	at java.util.AbstractQueue.add(AbstractQueue.java:98)
     * 	at com.concurrent.test.content.LinkedBlockingQueueTest$1.running(LinkedBlockingQueueTest.java:24)
     * 	at com.concurrent.utils.ForThreads$1.run(ForThreads.java:13)
     */
    private static void test5(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                arr.add("");
                arr.add("");
            }
        }.doWork(1);
    }
    /**
     * 线程0null
     */
    private static void test4(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                PrintUtils.log(String.valueOf(arr.poll()));

            }
        }.doWork(1);
    }
    /**
     * 线程0true
     * 线程0false
     */
    private static void test3(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(1);
        new ForThreads(){
            @Override
            protected void running() {
                    PrintUtils.log(String.valueOf(arr.offer("")));
                    PrintUtils.log(String.valueOf(arr.offer("")));

            }
        }.doWork(1);
    }
    private static void test2(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(2);
        new ForThreads(){
            @Override
            protected void running() {
                try {
                    arr.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.doWork(1);
    }
    private static void test1(){
        final LinkedBlockingQueue<String> arr = new LinkedBlockingQueue<>(2);
        new ForThreads(){
            @Override
            protected void running() {
                try {
                    arr.put("");
                    arr.put("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.doWork(2);
    }
}
