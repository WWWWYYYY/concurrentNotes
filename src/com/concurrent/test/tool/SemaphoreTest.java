package com.concurrent.test.tool;

import java.util.concurrent.Semaphore;

/**
 * 流量控制
 */
public class SemaphoreTest {

    private static Semaphore semaphore = new Semaphore(3);
    public static void main(String[] args) {
        for (int i=0;i<10;i++){
            Thread t =new Thread(){
                @Override
                public void run() {
                    try {
                        semaphore.acquire(1);
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
}
