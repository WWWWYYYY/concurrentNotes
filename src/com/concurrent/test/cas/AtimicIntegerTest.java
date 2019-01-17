package com.concurrent.test.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class AtimicIntegerTest {

    private static AtomicInteger num =new AtomicInteger(0);

    public static void main(String[] args) {
        for (int i=0;i<8;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    while (true) {
                        if (num.get()==100)break;
                        System.out.println(Thread.currentThread().getName()+":"+num.incrementAndGet());
                    }
                }
            };
            t.start();
        }
    }
}
