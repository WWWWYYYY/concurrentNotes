package com.concurrent.test.content.other;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 内部用ConcurrentSkipListMap
 */
public class ConcurrentSkipListSetTest {

    public static void main(String[] args) {

    }
    private static void test1(){
        ConcurrentSkipListSet<String> set =new ConcurrentSkipListSet<>();
        set.add("");
    }
}
