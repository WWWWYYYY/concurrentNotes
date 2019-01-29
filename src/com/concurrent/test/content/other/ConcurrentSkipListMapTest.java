package com.concurrent.test.content.other;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * TreeMap有序的容器，这两种容器的并发版本
 * 数据结构：单向链表 （多级）
 * 通过UNSAFE对象和自旋来保证安全的改变值
 */
public class ConcurrentSkipListMapTest {

    public static void main(String[] args) {

    }
    private static void  test1(){
        ConcurrentSkipListMap<String,String> map =new ConcurrentSkipListMap<>();
    }
}
