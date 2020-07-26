package com.concurrent.test.cas;

import com.concurrent.utils.PrintUtils;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * TODO
 */
public class AtomicMarkableReferenceTest {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        AtomicMarkableReference atomicMarkableReference = new AtomicMarkableReference("",false);
//        atomicMarkableReference.weakCompareAndSet()
        PrintUtils.log(""+atomicMarkableReference.isMarked());

    }
}  
