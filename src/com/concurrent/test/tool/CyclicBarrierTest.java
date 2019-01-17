package com.concurrent.test.tool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 场景1：将一个任务分解多个子任务，多个任务都完成后再继续完成最后的任务
 * 场景2：多个任务执行到某个程度以后才能继续执行
 */
public class CyclicBarrierTest {
    private static int count=4;
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(count, new Runnable() {
        @Override
        public void run() {
            System.out.println("屏障后的工作");
        }
    });

    public static void main(String[] args) {
        for (int i=0;i<count;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    try {
                        long time = (long) (Math.random()*10000);
                        System.out.println(Thread.currentThread().getName()+":休眠："+time);
                        Thread.sleep(time);
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
        System.out.println("main thread end");
    }


}
