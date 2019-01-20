package com.concurrent.test.tool;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {
    private static CountDownLatch countDownLatch =new CountDownLatch(3);
    public static void main(String[] args) throws InterruptedException {
        test2();
//        test1();
    }

    private static void test1() throws InterruptedException {
        new ForThreads(){
            @Override
            protected void running() {
                ThreadSleepTool.sleep(2000);
                countDownLatch.countDown();
            }
        }.doWork(1);

        PrintUtils.printCurrentThreadBlock();

        countDownLatch.await();//需要考虑子线程异常情况可能没有调用countDown（）方法  则需要中断等待或者其他处理

        PrintUtils.printCurrentThreadEnd();
    }
    private static void test2() throws InterruptedException {
        new ForThreads(){
            @Override
            protected void running() {
                ThreadSleepTool.sleep(2000);
                countDownLatch.countDown();
            }
        }.doWork(1);

        PrintUtils.printCurrentThreadBlock();
        if (!countDownLatch.await(3,TimeUnit.SECONDS)){
            PrintUtils.printCurrentThreadWaitInterrupted();
            return;
        }

        PrintUtils.printCurrentThreadEnd();
    }
}
