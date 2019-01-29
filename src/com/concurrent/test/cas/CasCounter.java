package com.concurrent.test.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 程数大于一定数量的话，多个线程在
 * 循环调用CAS接口，虽然不会让其他线程阻塞，但是这个时候竞争激烈，会导致CPU到达100%，同时比较耗时间，所以性能就不如synchronized了
 *
 * 并不是线程越多越好，要根据业务性质
 * 线程越多竞争也激烈
 *
 * cas一般要和volatile配合使用：使用场景尽量是读多写少。一个线程写多个线程读的情况
 * cas外层要使用for(;;){}自旋操作
 */
public class CasCounter  {

    private volatile long value = 1;
    private volatile long v = 1;

    private static Unsafe un;
    private static long valueOffset;
    private static long valueOffset_v;
    static
    {
        try{
            un = getUnsafeInstance();
//            un = Unsafe.getUnsafe();//获取不到对象
            valueOffset = un.objectFieldOffset(CasCounter.class.getDeclaredField("value"));//对象域偏移
            valueOffset_v = un.objectFieldOffset(CasCounter.class.getDeclaredField("v"));//对象域偏移
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

            if(value >= 100)
                return value;
            //unsafe是唯一一个可以直接读写主存的类
            if(un.compareAndSwapLong(this, valueOffset, current, next))//参数：对象,对象某个属性的偏移值，当前值，改变后的值
                System.out.println(value);
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
    private static void test1(){
    }

    public static void main(String[] args) {
        new CasCounter().increment();
    }
}