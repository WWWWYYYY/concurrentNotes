package com.concurrent.test.tool;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    private static CountDownLatch countDownLatch =new CountDownLatch(1);
    public static void main(String[] args) throws InterruptedException {
        Thread t =new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        };
        t.start();
        System.out.println("main thread block");
        countDownLatch.await();//需要考虑子线程异常情况可能没有调用countDown（）方法  则需要中断等待或者其他处理
        System.out.println("mian end");
    }


}
