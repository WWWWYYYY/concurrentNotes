package com.concurrent.test.lock;

import com.concurrent.test.ThreadSleepTool;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    public static void main(String[] args) {
    test1();
    }

    public static void test1(){
        final Random r = new Random();
        for (int i=0;i<10;i++){
            Thread t =new Thread(){
                @Override
                public void run() {
                    if (r.nextBoolean()){
                        readLock.lock();
                        System.out.println(Thread.currentThread().getName()+":获取到了读锁");
                        ThreadSleepTool.sleep(500);
                        System.out.println(Thread.currentThread().getName()+":释放了读锁");
                        readLock.unlock();
                    }else {
                        writeLock.lock();
                        System.out.println(Thread.currentThread().getName()+":获取到了写锁");
                        ThreadSleepTool.sleep(500);
                        System.out.println(Thread.currentThread().getName()+":释放了写锁");
                        writeLock.unlock();
                    }
                }
            };
            t.start();
        }
    }
}
