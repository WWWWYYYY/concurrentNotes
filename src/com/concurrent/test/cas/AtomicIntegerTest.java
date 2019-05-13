package com.concurrent.test.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {

    private static AtomicInteger num =new AtomicInteger(0);
//    private static final Unsafe unsafe = Unsafe.getUnsafe();

    public static void main(String[] args) {
        for (int i=0;i<8;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    while (true) {
                        if (num.get()==100)break;
                        System.out.println(Thread.currentThread().getName()+":"+num.incrementAndGet());//incrementAndGety已经是自旋+cas了
                    }
                }
            };
            t.start();
        }
    }
}
