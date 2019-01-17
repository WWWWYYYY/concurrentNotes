package com.concurrent.test.thread.cooperation;

/**
 * wait notify 一般配合synchronized 关键字使用
 * 调用wait方法之前需要获取锁，否则抛出异常。调用wait方法之后自动是否锁。
 * notify唤醒处于wait状态的第一个线程线程，多个线程处于等待同一个锁时会存在一个线程队列。notifyAll唤醒所有处于wait状态的线程
 */
public class WaitAndNotifyTest {
    static A instance =new A();
    static class A{
        public static int num =0;
        public synchronized void add() throws InterruptedException {
            num++;
            notify();//默认通知等待A类实例对象的线程
            wait();//默认让持有A类实例的线程处于等待，并释放实例锁
        }
    }

    public static void main(String[] args) throws InterruptedException {
        test1();
//        test2();
    }

    //两个交替打印;
    //ps：使用同步块的方式一定要指明哪个对象调用wait/notity。（切记）；如果使用synchronized修饰方法的方式 则在方法内就不需要指明哪个对象调用wait/notity
    public static void test1() throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                while (true)
                synchronized (A.class) {
                    System.out.println("t1:"+ ++A.num);
//                    notify(); 错误写法，此写法会释放WaitAndNotifyTest.class类锁，但是又没有获取到，则抛出异常
                    A.class.notify();
                    try {
                        A.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        };

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true)
                synchronized (A.class) {
                    System.out.println("t2:"+ ++A.num);
                    A.class.notify();
                    try {
                        A.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        t1.start();
        Thread.sleep(1000);
        t2.start();
    }

    /**
     * 调用synchronized修饰的方法时 不需要指明谁要调用，默认是A类实例
     */
    public static void test2() throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                while (true){
                    try {
                        instance.add();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        };

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true){
                    try {
                        instance.add();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }}
        };
        t1.start();
        Thread.sleep(1000);
        t2.start();
    }
}
