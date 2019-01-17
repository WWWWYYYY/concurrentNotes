package com.concurrent.test.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {

    private static AtomicStampedReference<String> atomicStampedReference =new AtomicStampedReference<>("abc",0);


    public static void main(String[] args) {

        atomicStampedReference.compareAndSet(
                atomicStampedReference.getReference(),
                "ddd",
                atomicStampedReference.getStamp(),
                atomicStampedReference.getStamp()+1
        );
        System.out.println(atomicStampedReference.getReference());
    }
}
