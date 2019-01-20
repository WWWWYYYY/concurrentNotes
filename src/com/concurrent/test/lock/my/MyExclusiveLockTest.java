package com.concurrent.test.lock.my;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class MyExclusiveLockTest {

private static MyExclusiveLock myExclusiveLock =new MyExclusiveLock();
private static Condition condition =myExclusiveLock.newCondition();
    public static void main(String[] args) throws InterruptedException {
        test2();
    }
    private static void test1(){

        new ForThreads(){
            @Override
            protected void running() {
                try {
                    if(!myExclusiveLock.tryLock(2,TimeUnit.SECONDS)){
                        PrintUtils.printCurrentThreadGiveUpLock();
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PrintUtils.printCurrentThreadGetLock();
                ThreadSleepTool.sleep(1000);
                PrintUtils.printCurrentThreadReleaseLock();
                myExclusiveLock.unlock();
            }
        }.doWork(5);
    }

    private static void test2() throws InterruptedException {

        new ForThreads(){
            @Override
            protected void running() {
                myExclusiveLock.lock();
                PrintUtils.printCurrentThreadWaiting();
                try {
                    condition.await();//方法内有释放锁的操作
                    PrintUtils.printCurrentThreadEnd();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    PrintUtils.printCurrentThreadReleaseLock();
                    myExclusiveLock.unlock();
                }
            }
        }.doWork(1);
        ThreadSleepTool.sleep(4000);
        myExclusiveLock.lock();
        condition.signal();
        myExclusiveLock.unlock();
        PrintUtils.printCurrentThreadEnd();
    }
}
