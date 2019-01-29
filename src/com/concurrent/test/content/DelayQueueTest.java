package com.concurrent.test.content;

import com.concurrent.utils.PrintUtils;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue内部使用了PriorityQueue所以也是无界的 ，每次存放的时候会根据时间排序，
 */
public class DelayQueueTest {

    /**
     * 固定使用这个模板，时间算的懵逼，只要知道t的单位是秒，元素存到到队列中ts才可以取出来。
     */
    private static class Item implements Delayed{
        //单位是秒
        public long t;
        String data;
        public Item(int t,String data){
            this.t=System.nanoTime()+TimeUnit.NANOSECONDS.convert(t,TimeUnit.SECONDS);//将时间最终换算成ns并且加上系统时间的ns
            this.data=data;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.t - System.nanoTime(), unit);
        }

        @Override
        public int compareTo(Delayed o) {//比较时换算成毫秒
            if (this.getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS)>=0) return 1;
            else return -1;
        }

        @Override
        public String toString() {
            return data;
        }
    }
    public static void main(String[] args) {

    }


    private static void test2(){
        DelayQueue<Item> queue =new DelayQueue<>();
        queue.put(new Item(3,"aaa"));
        queue.put(new Item(2,"bbb"));
        queue.put(new Item(5,"ccc"));
        try {
            while (true)
                PrintUtils.log(queue.take().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 时间换算1秒换算成其他单位
     */
    private static void test1(){
        System.out.println(TimeUnit.SECONDS.toNanos(1));//1s=1,000,000,000ns
        System.out.println(TimeUnit.SECONDS.toMillis(1));//1s=1,000 ms
        System.out.println(TimeUnit.SECONDS.toSeconds(1));//1s=1s
        System.out.println(TimeUnit.SECONDS.toMicros(1));//1s=1,000,000 微秒
    }
}
