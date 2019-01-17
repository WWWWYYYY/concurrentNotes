package com.concurrent.test.thread.create;

public class ThreadTest {

    public static void main(String[] args) {
        Thread t = new Thread(){
            @Override
            public void run() {
                int i =0;
                while (true){
                    System.out.println(i++);
                }
            }
        };
        t.start();
    }
}
