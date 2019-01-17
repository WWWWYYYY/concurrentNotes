package com.concurrent.test.thread.cooperation;

/**
 *
 */
public class JoinTest {

    static class MyThread extends Thread{
        private Runnable runnable;
        public MyThread(Runnable runnable ){
            this.runnable=runnable;
        }
        @Override
        public void run() {
            System.out.println("MyThread start");
            try {
                ((Thread)runnable).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("MyThread end");
        }
    }

    public static void main(String[] args) {
        test1();
    }

    public static void test1(){
        Thread t_join =new Thread(){
            @Override
            public void run() {
                System.out.println("t_join");
            }
        };
        MyThread myThread =new MyThread(t_join);
        myThread.start();
        t_join.start();//被join的线程也必须调用start方法后才能被join到其他线程中
    }
    public static void test2(){
        Thread t_join =new Thread(){
            @Override
            public void run() {
                System.out.println("t_join");
            }
        };
        MyThread myThread =new MyThread(t_join);
        t_join.start();//t_join被加入的线程已经执行完了则myThread中不会插入的join（）；不会生效；
        myThread.start();
    }
}
