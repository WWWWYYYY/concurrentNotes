package com.concurrent.test.lock.my;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 *
 * 1、实现lock接口
 * 2、定义内部类sync 继承AbstractQueuedSynchronizer抽象类
 * 3、重写tryAcquire、tryRelease
 * 该类 是一个简单排他锁 类似 synchronized+超时功能
 *
 * 要使用condition；要重写tryAcquire，tryRelease，isHeldExclusively。就从isHeldExclusively方法来看；condition必须和独占锁搭配
 *  * public final void signal() {
 *  *             if (!isHeldExclusively())必须是独占的情况下才能使用
 *  *                 throw new IllegalMonitorStateException();
 *  *
 *  *                 所以共享锁不需要提供condition功能
 */
public class MyExclusiveLock implements Lock {
    private Sync sync;
    private class Sync extends AbstractQueuedSynchronizer{

        public Sync(boolean isFair){this.isFair=isFair;}
        private boolean isFair;
        @Override
        protected boolean tryAcquire(int arg) {

            if (getExclusiveOwnerThread()==Thread.currentThread()){return true;}//可重入锁核心代码 当前线程是占有锁的线程，重复调用这个方法就不会阻塞在外面了
            else {
                if (isFair&&!hasQueuedPredecessors()){//公平锁的关键代码就是是否要从阻塞队列中先获取锁
                    return true;
                }
            }

            if (getExclusiveOwnerThread()==null){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getExclusiveOwnerThread()==Thread.currentThread()){
                setExclusiveOwnerThread(null);
                return true;
            }
            return super.tryRelease(arg);
        }
        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        //        该线程是否正在独占资源。只有用到condition才需要去实现它
        @Override
        protected boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

    }
    public MyExclusiveLock(){
        this.sync = new Sync(false);
    }
    public MyExclusiveLock(boolean isFair){

        this.sync = new Sync(isFair);
    }
    @Override
    public void lock() {
        sync.acquire(0);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(0);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(0);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(0);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
