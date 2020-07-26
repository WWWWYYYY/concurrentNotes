package com.concurrent.test.thread.sync;

/**
 *  获取同步锁的4种方式：
 *  1、static synchronized 静态方法+synchronized
 *  2、synchronized（SyncTest.class）同步块方式获取类锁
 *  3、synchronized  成员方法+synchronized
 *  4、synchronized(instance)    同步块方式获取实例锁
 *
 *  ps：某个线程使用任意方式获取了实例锁/类锁。另一个线程调用synchronized修饰的方法才会等待，如果调用了非synchronized修饰的方法则不等待。例子test5。
 */
public class SyncTest {
    static SyncTest instance =new SyncTest();


    static synchronized void m1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(3000);
    }
    void m() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(3000);
    }
    synchronized void m2() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(3000);
    }
    public static void main(String[] args) {
        test6();
    }

    //静态方法+synchronized 获取类锁
    public static void  test1(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                try {
                    SyncTest.m1();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                try {
                    SyncTest.m1();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
    }
    //synchronized代码块方式
    public static void  test2(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                synchronized (SyncTest.class){//同步块范围占用类锁
                    try {
                        System.out.println("t1 占用3s");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                try {
                    SyncTest.m1();//由于m1是类方法并且加了锁，所以需要等待其他线程释放类锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
    }
    //synchronized代码块方式 对象锁（实例锁）:多线程使用同一个实例调用成员方法时，遇到synchronized则遇到等待其他线程释放了实例锁才能调用
    public static void  test3(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                synchronized (instance){//同步块范围占用实例锁
                    try {
                        System.out.println("t1 占用instance 3s");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                try {
                    synchronized (instance){//同步块的意思是，必须锁以后才能执行同步块中的内容
                    instance.m();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
    }
    //synchronized代码块方式 对象锁（实例锁）:多线程使用同一个实例调用synchronized修饰的成员方法
    public static void  test4(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                synchronized (instance){//同步块范围占用实例锁
                    try {
                        System.out.println("t1 占用instance 3s");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                try {
                        instance.m2(); //m2使用了synchronized修饰，所以需要等待其他线程释放instance实例锁。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
    }

    public static void  test5(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                synchronized (instance){//同步块范围占用实例锁
                    try {
                        System.out.println("t1 占用instance 3s");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                try {
                    instance.m(); //m（）是普通的方法，调用时不需要先获取实例锁，所以不需要等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
    }

    /**
     * 锁不同的资源，不妨碍其他线程的执行，
     * 类锁影响不了实例锁，实例锁也影响不了类锁
     * 对象a和对象b的锁也是互不影响的
     */
    public static void  test6(){
        Thread t1 =new Thread("t1"){
            @Override
            public void run() {
                synchronized (instance){//同步块范围占用实例锁
                    try {
                        System.out.println("t1 占用instance 3s");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t2 =new Thread("t2"){
            @Override
            public void run() {
                synchronized (SyncTest.class){//同步块范围占用实例锁
                    try {
                        System.out.println("t2 占用instance 3s");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t2.start();
        t1.start();
    }
}
