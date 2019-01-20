package com.concurrent.test.tool;

import com.concurrent.utils.ThreadSleepTool;

import java.util.concurrent.Semaphore;

/**
 * 流量控制
 */
public class SemaphoreTest {

    private static Semaphore semaphore = new Semaphore(3);
    public static void main(String[] args) {
        test4();
    }
    //正常例子
    public static void test1(){
        for (int i=0;i<1;i++){
            Thread t =new Thread("线程"+i){
                @Override
                public void run() {
                    try {
                        semaphore.acquire(1);
                        semaphore.tryAcquire();
                        System.out.println(Thread.currentThread().getName()+"获取到资源");
                        Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName()+"释放了资源");
                        semaphore.release(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }
    public static void test2(){
        for (int i=0;i<1;i++){
            Thread t =new Thread("线程"+i){
                @Override
                public void run() {
                    try {
                        semaphore.acquire(4);
                        System.out.println(Thread.currentThread().getName()+"获取到资源");
                        Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName()+"释放了资源");
                        semaphore.release(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }
    public static void test3(){
            Thread t =new Thread("线程1"){
                @Override
                public void run() {
                    try {
                        semaphore.acquire(4);
                        System.out.println(Thread.currentThread().getName()+"获取到资源");
                        Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName()+"释放了资源");
                        semaphore.release(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();

        ThreadSleepTool.sleep(2000);
        Thread t_2 =new Thread("线程2"){
            @Override
            public void run() {
                try {
                    semaphore.acquire(4);
                    System.out.println(Thread.currentThread().getName()+"获取到资源");
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName()+"释放了资源");
                    semaphore.release(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t_2.start();
    }

    public static void test4(){
        Thread t_2 =new Thread("线程2"){
            @Override
            public void run() {
                try {
                    semaphore.acquire(4);
                    System.out.println(Thread.currentThread().getName()+"获取到资源");
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName()+"释放了资源");
                    semaphore.release(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t_2.start();
        Thread t =new Thread("线程1"){
            @Override
            public void run() {
                try {
                    semaphore.acquire(1);
                    System.out.println(Thread.currentThread().getName()+"获取到资源");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+"释放了资源");
                    semaphore.release(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();


    }
}
