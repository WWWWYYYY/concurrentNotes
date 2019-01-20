package com.concurrent.utils;

public class PrintUtils {

    public static void printCurrentThreadGetLock(){
        System.out.println(Thread.currentThread().getName()+"获取到锁");
    }

    public static void printCurrentThreadReleaseLock(){
        System.out.println(Thread.currentThread().getName()+"释放了锁");
    }
    public static void printCurrentThreadGiveUpLock(){
        System.out.println(Thread.currentThread().getName()+"放弃了获取锁");
    }
    public static void printCurrentThreadBlock(){
        System.out.println(Thread.currentThread().getName()+"阻塞了");
    }
    public static void printCurrentThreadWaiting(){
        System.out.println(Thread.currentThread().getName()+"等待中");
    }
    public static void printCurrentThreadEnd(){
        System.out.println(Thread.currentThread().getName()+"调用结束");
    }

    public static void printCurrentThreadWaitInterrupted() {
        System.out.println(Thread.currentThread().getName()+"等待过程中被中断");
    }

    public static void printCurrentThreadCountDown() {
        System.out.println(Thread.currentThread().getName()+"invoke countDown");
    }
}
