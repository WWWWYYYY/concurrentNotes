package com.concurrent.test.lock;

import com.concurrent.test.ThreadSleepTool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    private static ReentrantLock reentrantLock =new ReentrantLock(false);
    private static CyclicBarrier cyclicBarrier =new CyclicBarrier(10);
    public static void main(String[] args) {
        test3();
//        test2();
//        test1();
    }

    public static void test1(){
        for (int i =0;i<10;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    ThreadSleepTool.sleep((long) (Math.random()*1000));
                    try {
                        cyclicBarrier.await();//同时让10个线程阻塞到这里，但是阻塞也是有先后的
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    reentrantLock.lock();//每次只有一个线程能够获取到锁
                    System.out.println(Thread.currentThread().getName()+":获取到锁");
                    ThreadSleepTool.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+":释放锁");
                    reentrantLock.unlock();//显示锁必须手动释放
                }
            };
            t.start();
        }
    }

    public static void test2(){
        for (int i =0;i<10;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    ThreadSleepTool.sleep((long) (Math.random()*1000));
                    try {
                        cyclicBarrier.await();//同时让10个线程阻塞到这里，但是阻塞也是有先后的
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (reentrantLock.tryLock(1,TimeUnit.SECONDS)){
                            System.out.println(Thread.currentThread().getName()+":获取到锁");
                            ThreadSleepTool.sleep(500);
                            System.out.println(Thread.currentThread().getName()+":释放锁");
                            reentrantLock.unlock();//显示锁必须手动释放
                        }else {
                            System.out.println(Thread.currentThread().getName()+":放弃了获取锁");
                        }
                        //每次只有一个线程能够获取到锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }

    /**
     * 尝试获取锁过程中被中断了
     */
    public static void test3(){
        reentrantLock.lock();
        Thread t =new Thread(){
            @Override
            public void run() {
                try {
                    reentrantLock.tryLock(3,TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName()+":被中断了。。。");
                }
            }
        };
        t.start();
        ThreadSleepTool.sleep(1000);
        t.interrupt();
    }
}
