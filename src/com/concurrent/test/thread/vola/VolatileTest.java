package com.concurrent.test.thread.vola;

/**
 * volatile只能保证可见性，不能保证原子性，所以多线程同时做num++操作存在问题，锁能保证原子性
 * 建议：只能有一个线程修改volatile修饰的变量，其他线程只能是访问。
 * ps:具体为什么是可见性需要查阅java内存模型中volatile修饰的变量在内存中的位置和线程访问volatile修饰的变量的原理。
 */
public class VolatileTest {
    public volatile static int num =0;



    public static void main(String[] args) {
//        test1();
        test2();
    }

    /**
     * 多个线程是实时访问volatile修饰的变量的最新值。
     */
    public static void test1(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                while (true)
                try {
                    Thread.sleep(1000);
                    System.out.println("t1:"+ ++num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                while (true)
                try {
                    Thread.sleep(1000);
                    System.out.println("t2:"+num);//访问的是最新的数据
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
    }

    /**
     * 对于volatile修饰的变量 多线程同时修改则出现数据不安全的情况
     */
    public static void test2(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                while (true)
                    try {
                        Thread.sleep(1000);
                        System.out.println("t1:"+ ++num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                while (true)
                    try {
                        Thread.sleep(1000);
                        System.out.println("t2:"+ ++num);//访问的是最新的数据
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        };

        t1.start();
        t2.start();
    }
}
