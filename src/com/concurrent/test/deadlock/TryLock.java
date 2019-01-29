package com.concurrent.test.deadlock;

import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TryLock {

    private static class User{
        public  ReentrantLock lock =new ReentrantLock();
        public String name;
        public int money;

        public User(String name, int money) {
            this.name = name;
            this.money = money;
        }
    }

    public static void main(String[] args) {
        test1();
    }

    private static void test1(){
        final User zhangsan =new User("zhangsan",50);
        final User lisi =new User("lisi",100);
        final int money =100;
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                transMoneyByLock(zhangsan,lisi,money);
            }
        });
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                transMoneyByLock(lisi,zhangsan,money);
            }
        });

        t1.start();
        t2.start();
    }

    /**
     * 通过活锁的方式：当获取了部分锁，获取下一把锁失败则放弃当前已有的资源。然后重新竞争。显然效率不高，当锁的资源种类变多，竞争线程数量也很多的时候效率就很低了
     *
     * 推荐使用{@link DeadLockTest#transMoneyUpgrade(String,String,int)}
     */
    private static boolean transMoneyByLock(User from, User to, int money) {
        Random r = new Random();
        while (true){
            if (from.lock.tryLock()){
                PrintUtils.log("获取了"+from.name+"的锁");
                ThreadSleepTool.sleep(10);
                if (to.lock.tryLock()){
                    PrintUtils.log("获取了"+to.name+"的锁");
                    PrintUtils.log(from.name+"转账给"+to.name+":"+money);
                    PrintUtils.log("释放"+to.name+"的锁");
                    PrintUtils.log("释放"+from.name+"的锁");
                    to.lock.unlock();
                    from.lock.unlock();
                    return true;
                }else {
                    PrintUtils.log("放弃了"+from.name+"的锁");
                    from.lock.unlock();
                    ThreadSleepTool.sleep(r.nextInt(1000));
                }
            }else {
                ThreadSleepTool.sleep(r.nextInt(1000));
            }
        }
    }
}
