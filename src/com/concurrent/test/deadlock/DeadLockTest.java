package com.concurrent.test.deadlock;

import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

public class DeadLockTest {

    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }



    //解决死锁 升级版转账方法 让锁住的资源有固定的顺序
    public static boolean transMoneyUpgrade(String fromName, String toName, int money) {
        int formhash =fromName.hashCode();
        int tohash =toName.hashCode();
        if (formhash>tohash){
            synchronized (fromName) {
                ThreadSleepTool.sleep(500);//模拟业务代码执行消耗500ms
                synchronized (toName) {
                    PrintUtils.log(fromName + "转账给" + toName + ":" + money);

                }
            }
        }else if (formhash<tohash){
            synchronized (toName) {
                ThreadSleepTool.sleep(500);//模拟业务代码执行消耗500ms
                synchronized (fromName) {
                    PrintUtils.log(fromName + "转账给" + toName + ":" + money);

                }
            }
        }else {//相等的时候有概率出现死锁。概率极低
            synchronized (toName) {
//                ThreadSleepTool.sleep(500);//模拟业务代码执行消耗500ms  //尽量把获取多个锁的操作凑在一起，把耗时代码转移到获取两把锁后的代码块中
                synchronized (fromName) {
                    ThreadSleepTool.sleep(500);//模拟业务代码执行消耗500ms
                    PrintUtils.log(fromName + "转账给" + toName + ":" + money);

                }
            }
        }

        return true;
    }
    private static void test3(){
        final String zhangsan = "zhangsan";
        final String lisi = "lisi";
        final int money = 100;
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                transMoneyUpgrade(zhangsan,lisi,money);
            }
        });
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                transMoneyUpgrade(lisi,zhangsan,money);
            }
        });

        t1.start();
        t2.start();
    }


    private static boolean transMoney(String fromName, String toName, int money) {
        synchronized (fromName) {
            ThreadSleepTool.sleep(500);//模拟业务代码执行消耗500ms
            synchronized (toName) {
                PrintUtils.log(fromName + "转账给" + toName + ":" + money);

            }
        }
        return true;
    }

    /**
     * 死锁原因：竞争资源通过传递的方式传到方法里并且传递的竞争资源元素大于1个。
     * 死锁堆栈
     * ===================================================
     * "Thread-1":
     * 	at com.concurrent.test.deadlock.DeadLockTest.transMoney(DeadLockTest.java:17)
     * 	- waiting to lock <0x000000076adb0b78> (a java.lang.String)
     * 	- locked <0x000000076adb0bb0> (a java.lang.String)
     * 	at com.concurrent.test.deadlock.DeadLockTest.access$000(DeadLockTest.java:6)
     * 	at com.concurrent.test.deadlock.DeadLockTest$2.run(DeadLockTest.java:37)
     * 	at java.lang.Thread.run(Thread.java:748)
     * "Thread-0":
     * 	at com.concurrent.test.deadlock.DeadLockTest.transMoney(DeadLockTest.java:17)
     * 	- waiting to lock <0x000000076adb0bb0> (a java.lang.String)
     * 	- locked <0x000000076adb0b78> (a java.lang.String)
     * 	at com.concurrent.test.deadlock.DeadLockTest.access$000(DeadLockTest.java:6)
     * 	at com.concurrent.test.deadlock.DeadLockTest$1.run(DeadLockTest.java:31)
     * 	at java.lang.Thread.run(Thread.java:748)
     *
     * Found 1 deadlock.
     */
    private static void test2() {
        final String zhangsan = "zhangsan";
        final String lisi = "lisi";
        final int money = 100;
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                transMoney(zhangsan,lisi,money);
            }
        });
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                transMoney(lisi,zhangsan,money);
            }
        });

        t1.start();
        t2.start();
    }

    /**
     * 死锁的根本成因：**获取锁的顺序不一致导致。**
     * **解决办法：保证加锁的顺序性**
     * 死锁日志：
     * =============================
     * "Thread-1":
     * waiting to lock monitor 0x00007fe18c831b58 (object 0x000000076adaf828, a java.lang.String),
     * which is held by "Thread-0"
     * "Thread-0":
     * waiting to lock monitor 0x00007fe18c82f168 (object 0x000000076adaf840, a java.lang.String),
     * which is held by "Thread-1"
     * <p>
     * Java stack information for the threads listed above:
     * ===================================================
     * "Thread-1":
     * at com.concurrent.test.deadlock.DeadLockTest$2.run(DeadLockTest.java:33)
     * - waiting to lock <0x000000076adaf828> (a java.lang.String)
     * - locked <0x000000076adaf840> (a java.lang.String)
     * "Thread-0":
     * at com.concurrent.test.deadlock.DeadLockTest$1.run(DeadLockTest.java:22)
     * - waiting to lock <0x000000076adaf840> (a java.lang.String)
     * - locked <0x000000076adaf828> (a java.lang.String)
     * <p>
     * Found 1 deadlock.
     */
    private static void test1() {
        final String s1 = new String();
        final String s2 = new String();

        Thread t1 = new Thread() {
            @Override
            public void run() {
                synchronized (s1) {
                    ThreadSleepTool.sleep(1000);
                    synchronized (s2) {

                    }
                }
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                synchronized (s2) {
                    ThreadSleepTool.sleep(1000);
                    synchronized (s1) {

                    }
                }
            }
        };
        t1.start();
        t2.start();
    }
}
