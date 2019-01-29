package com.concurrent.test.content.other;

import com.concurrent.utils.PrintUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 无界非阻塞队列，底层是一个链表，先进先出
 *
 * add,offer将元素插入到尾部，peek（拿头部的数据，但是不移除）和poll（拿头部的数据，但是移除）
 *
 * LinkedList并发版本
 *
 * 正是没有阻塞的概念所以底层不需要aqs进行线程阻塞管理，非阻塞也就意味着要马上知道 add/remove/offer/poll的结果，这种想马上知道结果的操作就需要cas的方式来解决，也就是UNSAFE
 */
public class ConcurrentLinkedQueueTest {


    public static void main(String[] args) {
        test1();
    }

    private static void test1(){
        ConcurrentLinkedQueue<String> queue =new ConcurrentLinkedQueue<>();

        PrintUtils.log(queue.poll());//没有元素时返回null
//        PrintUtils.log(queue.remove());//没有元素时抛异常
    }
}
