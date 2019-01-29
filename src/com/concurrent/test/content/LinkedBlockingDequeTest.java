package com.concurrent.test.content;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * LinkedBlockingQueue 是单向队列 两把锁
 * LinkedBlockingDeque 是双向队列 一把锁
 * 都是使用可重入锁，基本上是一样的，就只有单向队列和双向队列的操作不一样，双向队列可以从队列头部操作。
 * 不管队列有界还是无界，使用的时候尽量设置大小。
 * 都一样把元素封装了Node对象
 *
 */
public class LinkedBlockingDequeTest {

    public static void main(String[] args) {

    }

    private static void test1(){
        LinkedBlockingDeque<String> deque =new LinkedBlockingDeque<>(1);
        deque.pop();
        deque.push("");
    }
}
