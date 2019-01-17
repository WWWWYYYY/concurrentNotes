package com.concurrent.test.cas;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArrayTest {

    private static AtomicIntegerArray array =new AtomicIntegerArray(new int[]{1,2,3,4});

    public static void main(String[] args) {
        array.incrementAndGet(0);
        System.out.println(array.get(0));
    }
}
