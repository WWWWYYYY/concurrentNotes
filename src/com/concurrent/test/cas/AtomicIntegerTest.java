package com.concurrent.test.cas;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;
import com.concurrent.utils.ThreadsProcessUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {

    private static AtomicInteger num =new AtomicInteger(0);
//    private static final Unsafe unsafe = Unsafe.getUnsafe();

    public static void main(String[] args) {
        test2();
    }

    /**
     * updateAndGet方法中已经添加了自旋
     */
    private static void test2() {

        ThreadsProcessUtil.doWork(()->{
            num.updateAndGet(s->{
                PrintUtils.log("s:"+s);
                return s+1;
            });
        },100);
        ThreadSleepTool.sleep(1000);
        System.out.println(num.get());
    }

    private static void test1() {
        for (int i=0;i<8;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    while (true) {
                        if (num.get()==100)break;
                        System.out.println(Thread.currentThread().getName()+":"+num.incrementAndGet());//incrementAndGety已经是自旋+cas了
                    }
                }
            };
            t.start();
        }
    }
}
