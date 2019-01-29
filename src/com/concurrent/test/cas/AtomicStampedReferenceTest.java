package com.concurrent.test.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {

    private static AtomicStampedReference<String> atomicStampedReference =new AtomicStampedReference<>("abc",0);


    public static void main(String[] args) {

        for(;;){
            boolean result =atomicStampedReference.compareAndSet(
                    atomicStampedReference.getReference(),
                    "ddd",
                    atomicStampedReference.getStamp(),
                    atomicStampedReference.getStamp()+1
            );
            if (result)break;
        }

        System.out.println(atomicStampedReference.getReference());
    }
}
