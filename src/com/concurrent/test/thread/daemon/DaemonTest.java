package com.concurrent.test.thread.daemon;

/**
 * 守护线程和程序同生共死
 * 实际例子：垃圾回收线程、引用回收线程。
 */
public class DaemonTest {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread("守护线程"){
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("守护线程执行中。。。");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        t.setDaemon(true);//守护线程一定要在启动之前把线程设置为守护线程，不能把一个已启动的非守护线程设置成守护线程。
        t.start();
        Thread.sleep(5000);
    }
}
