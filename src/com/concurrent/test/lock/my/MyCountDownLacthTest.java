package com.concurrent.test.lock.my;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

public class MyCountDownLacthTest {

    private static MyCountDownLacth myCountDownLacth =new MyCountDownLacth(2);
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    private static void test1() throws InterruptedException {
        new ForThreads(){
            @Override
            protected void running() {
                ThreadSleepTool.sleep(1000);
                PrintUtils.printCurrentThreadCountDown();
                myCountDownLacth.countDown();
            }
        }.doWork(2);

        PrintUtils.printCurrentThreadBlock();
        myCountDownLacth.await();
        PrintUtils.printCurrentThreadEnd();
    }
}
