package com.concurrent.test.thread.cooperation;

/**
 * 使用join方法，说明a线程有b线程的引用
 *
 * join方法实质上是让当前线程挂起把占用cpu时间让给其他线程
 *
 * join作用之一使得线程间可以串行，也使得多线程设计变得复杂很多
 */
public class JoinTest {

    static class MyThread extends Thread{
        private Runnable runnable;
        public MyThread(Runnable runnable ){
            this.runnable=runnable;
        }
        @Override
        public void run() {
            System.out.println("MyThread start"+getName());
            try {
                Thread.sleep(1000);
                System.out.println("MyThread start2"+getName());
                ((Thread)runnable).join(10);
                System.out.println("MyThread start3"+getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("MyThread end"+getName());
        }
    }

    public static void main(String[] args) {
        test1();
    }

    public static void test1(){
        Thread t_join =new Thread("t_join"){
            @Override
            public void run() {
                int i =0;
                while (i<100){
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t_join"+getName());
                    i++;
                }
            }
        };
        MyThread myThread =new MyThread(t_join);
        myThread.setName("myThread");
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
