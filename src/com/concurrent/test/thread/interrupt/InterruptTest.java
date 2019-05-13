package com.concurrent.test.thread.interrupt;

/**
 * 中断线程可以中断等待状态下线程（sleep、wait）；
 */
public class InterruptTest {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(){
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try {

                        Thread.sleep(1000);
                        System.out.println("123");
                    } catch (InterruptedException e) {//当cpu接收到信号对线程进行中断时，会马上被捕获，不管上面的代码执行到哪一步。这是遵循happens-before的规则
                        e.printStackTrace();
                        Thread.currentThread().interrupt();//中断异常后  线程的中断状态将被重置变为false。
                    }
                }
            }
        };
        t.start();
        Thread.sleep(2500);//t线程运行到第三轮休眠的过程中被中断
        System.out.println("2.5s后中断线程T");
        t.interrupt();//线程中断仅仅是发信号给cpu中断线程的执行，但是不能确定cpu是否立刻中断线程，所以线程之间是协作的，仅仅能想cpu发送信号，让cpu全权处理。
    }
}
