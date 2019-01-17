package com.concurrent.test.thread.state;

/**
 * 线程5大状态：创建、就绪、运行、阻塞、死亡
 */
public class ThreadStateChangeTest {

    public static void main(String[] args) {
        test2();
    }

    //创建》就绪》运行》死亡
    public static void test1() {
        Thread t = new Thread(){//创建
            @Override
            public void run() {//运行
                System.out.println("running");
            }
            //run方法执行完就是变更为死亡状态
        };

        t.start();//就绪
    }
    //创建》就绪》运行》阻塞》就绪》运行》死亡
    public static void test2() {
        Thread t = new Thread(){//创建
            @Override
            public void run() {//运行
                System.out.println("running");
                System.out.println("阻塞");
                try {
                    Thread.sleep(1000);//进入阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("runnning end");
                Thread.currentThread().stop();//主动调用stop方法
                System.out.println("1221");
            }
            //run方法执行完就是变更为死亡状态
        };

        t.start();//就绪
    }

    //创建》就绪》运行》就绪》运行》死亡
    public static void test3() {
        Thread t = new Thread(){//创建
            @Override
            public void run() {//运行
                System.out.println("running");
                System.out.println("阻塞");
                Thread.yield();//将cpu控制权让给其他线程，并让当前线程处于就绪状态
                System.out.println("runnning end");
                Thread.currentThread().stop();//主动调用stop方法
                System.out.println("1221");
            }
            //run方法执行完就是变更为死亡状态
        };

        t.start();//就绪
    }

}
