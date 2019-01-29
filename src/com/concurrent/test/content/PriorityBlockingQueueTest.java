package com.concurrent.test.content;

import com.concurrent.utils.PrintUtils;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * PriorityBlockingQueue相比ArrayBlockingQueue:
 * PriorityBlockingQueue可以是无界的，但是不推荐不设置大小，主要在put操作上多了一个排序的操作。底层也是都只用一把可重入锁
 */
public class PriorityBlockingQueueTest {

    private static class Item implements Comparable{
        private int num;
        public Item(int num){
            this.num=num;
        }
        @Override
        public int compareTo(Object o) {
            Item item = (Item) o;
            if (item.num==this.num)
            return 0;
            else if(item.num<this.num) return 1;
                else return -1;
        }

        @Override
        public String toString() {
            return String.valueOf(num);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    /**
     * 对于队列尽量使用有界，防止内存溢出
     */
    private static void test1() throws InterruptedException {
        PriorityBlockingQueue<Item> queue=new PriorityBlockingQueue<>(10);
        queue.put(new Item(5));
        queue.put(new Item(3));
        queue.put(new Item(0));//每次存放加锁并排序数组
        PrintUtils.log(String.valueOf(queue.take()));
        PrintUtils.log(String.valueOf(queue.take()));
        PrintUtils.log(String.valueOf(queue.take()));
    }
}
