package com.concurrent.test.lock.my;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 共享锁 获取锁和释放锁都是通过cas的方式 调用包含Shared关键字的方法
 *
 * 共享锁下的condition；要重写tryAcquire，tryRelease，isHeldExclusively。就从isHeldExclusively方法来看；condition必须和独占锁搭配
 * public final void signal() {
 *             if (!isHeldExclusively())必须是独占的情况下才能使用
 *                 throw new IllegalMonitorStateException();
 *
 *                 所以共享锁不需要提供condition功能
 */
public class MySharedLock implements Lock {

    private Sync sync;

    public MySharedLock(int permits){
        sync =new Sync(permits);
    }

    private class Sync extends AbstractQueuedSynchronizer{

        public Sync(int permits){
            setState(permits);
        }
        @Override
        protected int tryAcquireShared(int permits) {
            if (permits < 0) throw new IllegalArgumentException();
           for (;;){
               int st =getState();
               int remain =st-permits;
               if (remain<0||compareAndSetState(st,remain)){
                   return remain;//如果remain在没有cas循环下就已经是<0返回时就会调用dotryAcquireShared后阻塞，如果remain>0并且设置成功就返回请求成功
               }
           }
        }

        @Override
        protected boolean tryReleaseShared(int permits) {
            if (permits < 0) throw new IllegalArgumentException();
            for(;;){
                int st =getState();
                int remain =st+permits;
                if (remain<st) // overflow
                    throw new Error("Maximum permit count exceeded");
                if (compareAndSetState(st,remain)){
                    return true;
                }
            }
        }
        //        该线程是否正在独占资源。只有用到condition才需要去实现它
//        @Override
//        protected boolean isHeldExclusively() {
//            return super.isHeldExclusively();
//        }
    }
    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1)>0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}
