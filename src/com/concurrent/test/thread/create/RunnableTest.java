package com.concurrent.test.thread.create;

public class RunnableTest {
    public static void main(String[] args) {
        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("123");
            }
        });
        t.start();
    }
}
