package com.concurrent.test.cas;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * 程数大于一定数量的话，多个线程在
 * 循环调用CAS接口，虽然不会让其他线程阻塞，但是这个时候竞争激烈，会导致CPU到达100%，同时比较耗时间，所以性能就不如synchronized了
 *
 * 并不是线程越多越好，要根据业务性质
 * 线程越多竞争也激烈
 */
public class CasCounter  {

    private volatile long value = 0;

    private static Unsafe un;
    private static long valueOffset;
    static
    {
        try{
            un = getUnsafeInstance();
            valueOffset = un.objectFieldOffset(CasCounter.class.getDeclaredField("value"));
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println("init unsafe error!");
        }
    }
    public long getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    public long increment() {
        // TODO Auto-generated method stub
        long current;
        long next;

        for(;;)
        {
            current = value;
            next = current + 1;

            if(value >= 100000000)
                return value;
            if(un.compareAndSwapLong(this, valueOffset, current, next))
                return next;
        }
    }

    private static Unsafe getUnsafeInstance() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        return (Unsafe) theUnsafeInstance.get(Unsafe.class);
    }

}