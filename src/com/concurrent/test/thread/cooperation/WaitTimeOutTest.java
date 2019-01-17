package com.concurrent.test.thread.cooperation;

/**
 * wait notify 超时模式
 */
public class WaitTimeOutTest {
    public static String abc = "";

    public static void main(String[] args) throws InterruptedException {


        Thread t = new Thread("t1") {
            @Override
            public void run() {
                long max_wait_time = 5000l;
                synchronized (WaitTimeOutTest.class) {
                    long time = System.currentTimeMillis() + max_wait_time;

                    while (time - System.currentTimeMillis() > 0) {
                        max_wait_time = time - System.currentTimeMillis();
                        System.out.println("t1 开始等待：" + max_wait_time);//重点:被唤醒后需要重新计算等待时间
                        try {
                            WaitTimeOutTest.class.wait(max_wait_time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("等待超时");
                }
            }
        };

        t.start();
        Thread.sleep(1000);
        synchronized (WaitTimeOutTest.class){
            WaitTimeOutTest.class.notify();//提醒等待WaitTimeOutTest.class类锁
        }
        Thread.sleep(1000);
        synchronized (WaitTimeOutTest.class){
            WaitTimeOutTest.class.notify();
        }
    }
}
