package com.concurrent.test.thread.local;

/**
 * 每个线程都有独立的ThreadLocal副本，不会相互影响，在thread类中每个线程绑定了一个threadlocal，所以特别要注意线程池中的线程不能乱用threadlocal来存数据
 */
public class ThreadLocalTest {

    static class MyThread extends Thread{
        public MyThread(String name) {
            super(name);
        }

        private  ThreadLocal<String> name=new ThreadLocal<>();
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            name.set(Thread.currentThread().getName());
            System.out.println(name.get());
        }
    }

    public static void main(String[] args) {
        MyThread t1 =new MyThread("t1");
        MyThread t2 =new MyThread("t2");
        t1.start();
        t2.start();
    }
}
