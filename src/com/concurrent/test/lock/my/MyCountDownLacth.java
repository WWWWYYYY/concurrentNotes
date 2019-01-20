package com.concurrent.test.lock.my;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyCountDownLacth {
    private final class Sync extends AbstractQueuedSynchronizer{

        public Sync(int perimits){
            setState(perimits);
        }
        @Override
        protected int tryAcquireShared(int arg) {
            return (getState()==0)?1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;){
                int st =getState();
                if (st==0){
                    return false;
                }
                int remian =st-arg;
                if (compareAndSetState(st,remian))
                    return remian==0;

            }

        }

        public long getCount() {
            return getState();
        }
    }

    private final Sync sync;
    public MyCountDownLacth(int count){
        if (count < 0) throw new IllegalArgumentException();
        sync = new Sync(count);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1,unit.toNanos(timeout));
    }

    public void countDown() {
        sync.releaseShared(1);
    }

    public long getCount() {
        return sync.getCount();
    }
}
