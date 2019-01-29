package com.concurrent.test.security;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

public class UnsecurityTest {


    private static  int num =0;

    public static class A{
        public int age=0;
    }

    private static A a= new  A();
    public static void main(String[] args) {
       test2();
    }

    /**
     * 就算是引用类型里的属性，也是一样的概念。因为线程操作基本类型时总是要进行入栈出栈操作。这时操作基本类型都会复制一个副本。等操作结束以后再把值写入到主存中。
     */
    private static void test2(){
        new ForThreads(){
            @Override
            protected void running() {
                while (a.age++<20){
                    ThreadSleepTool.sleep(1);
                    PrintUtils.log("num:"+String.valueOf(a.age));
                }
            }
        }.doWork(4);
    }

    /**
     * 线程2------num:4
     * 线程3------num:4
     * 线程1------num:4
     * 线程0------num:4
     * 线程2------num:8
     * 线程1------num:8
     * 线程3------num:10
     * 线程0------num:10
     * 线程1------num:12
     * 线程2------num:13
     * 线程0------num:14
     * 线程3------num:15
     * 线程1------num:16
     * 线程2------num:17
     * 线程0------num:17
     * 线程3------num:18
     * 线程1------num:20
     * 线程2------num:21
     * 线程0------num:21
     * 线程3------num:21
     *
     * 由于num没有volatile修饰，所以其他线程在num被修改后不可见
     * 并且num++并不是一个原子操作。
     * 至少在指令上有三步 load num ；add；store num；如果全部线程都同时load num 那么 多个线程在执行add指令的时候就会导致数据错误（并发不安全）
     */
    private static void test1(){
        new ForThreads(){
            @Override
            protected void running() {
                while (num++<20){
                    ThreadSleepTool.sleep(1);
                    PrintUtils.log("num:"+String.valueOf(num));
                }
            }
        }.doWork(4);
    }
}
