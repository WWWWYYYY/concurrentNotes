package com.concurrent.test;

public class ThreadSleepTool {

    public static void sleep(long n){
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
