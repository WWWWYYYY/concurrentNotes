package com.concurrent.test.content.other;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * CopyOnWriteArraySet 内部用的CopyOnWriteArrayList
 *
 * 这两个容器 都是写时加锁，读时不加锁；所以读的速度比较快，但是会读到旧数据；看需求的需要来选择
 */
public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {

    }
    private static void test1(){
        CopyOnWriteArrayList<String> arrayList =new CopyOnWriteArrayList<>();
        arrayList.add("");
        arrayList.remove(1);
        CopyOnWriteArraySet<String> set =new CopyOnWriteArraySet();
        set.add("");
    }
}
