package com.concurrent.practice.framework.vo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayVo<T> implements Delayed{
    //单位是毫秒
    public long activeTime;
    T data;
    public DelayVo(long activeTime,T data){
        this.activeTime=System.nanoTime()+TimeUnit.NANOSECONDS.convert(activeTime,TimeUnit.MILLISECONDS);//将时间最终换算成ns并且加上系统时间的ns
        this.data=data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.activeTime - System.nanoTime(), unit);
    }

    @Override
    public int compareTo(Delayed o) {//比较时换算成毫秒
        if (this.getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS)>=0) return 1;
        else return -1;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}