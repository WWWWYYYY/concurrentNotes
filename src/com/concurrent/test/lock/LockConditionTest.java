package com.concurrent.test.lock;

import com.concurrent.utils.ThreadSleepTool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 相比于notify/wait的使用，condition更加细致化。一个锁可以创建多个condition，多个线程不再为通一把锁激烈争夺，更加合理、有目标性的唤醒等待中的线程。
 */
public class LockConditionTest {

    private static ReentrantLock reentrantLock = new ReentrantLock(false);
    private static final Condition condition = reentrantLock.newCondition();
    private static final Condition condition2 = reentrantLock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        test5();
    }

    public static void test1() {
        Thread t_1 = new Thread() {
            @Override
            public void run() {
                reentrantLock.lock();
                try {
                    try {
                        condition.await();
                        System.out.println("condition");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        };
        Thread t_2 = new Thread() {
            @Override
            public void run() {
                reentrantLock.lock();
                try {
                    try {
                        condition2.await();
                        System.out.println("condition2");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        };
        t_1.start();
        t_2.start();
        ThreadSleepTool.sleep(100);
        reentrantLock.lock();
        condition2.signal();
        reentrantLock.unlock();

    }

    public static void test2() {
        Thread t_1 = new Thread() {
            @Override
            public void run() {
                reentrantLock.lock();
                try {
                    try {
                        if (condition.await(1, TimeUnit.SECONDS)) {
                            System.out.println("condition");

                        }else {
                            System.out.println("t_1 waitTimeOut...");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        };
        Thread t_2 = new Thread() {
            @Override
            public void run() {
                reentrantLock.lock();
                try {
                    try {
                        condition2.await(5000,TimeUnit.MILLISECONDS);
                        System.out.println("condition2");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        };
        t_1.start();
        t_2.start();
        ThreadSleepTool.sleep(100);
        reentrantLock.lock();
        condition2.signal();
        reentrantLock.unlock();

    }

    public static void test5() throws InterruptedException {
        reentrantLock.lock();
                    condition.await();

    }
}
