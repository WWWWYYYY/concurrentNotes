package com.concurrent.test.lock.my;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

public class MySharedLockTest {

    private static MySharedLock mySharedLock = new MySharedLock(3);
    //共享锁不支持condition
//    private static Condition condition = mySharedLock.newCondition();

    public static void main(String[] args) {
        test1();
//        test2();
    }

    private static void test1() {

        new ForThreads() {
            @Override
            public void running() {
                mySharedLock.lock();
                PrintUtils.printCurrentThreadGetLock();
                ThreadSleepTool.sleep(1000);
                mySharedLock.unlock();
                PrintUtils.printCurrentThreadReleaseLock();
            }
        }.doWork(10);

    }
//共享锁不支持condition
   /* private static void test2() {

        new ForThreads() {
            @Override
            public void running() {
                mySharedLock.lock();
                PrintUtils.printCurrentThreadBlock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    PrintUtils.printCurrentThreadReleaseLock();
                    mySharedLock.unlock();
                }
            }
        }.doWork(1);

        ThreadSleepTool.sleep(4000);
        mySharedLock.lock();
        condition.signal();
        mySharedLock.unlock();
        PrintUtils.printCurrentThreadEnd();
    }*/

}
