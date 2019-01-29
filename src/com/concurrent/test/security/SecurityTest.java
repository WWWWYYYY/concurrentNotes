package com.concurrent.test.security;

import com.concurrent.utils.ForThreads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecurityTest {


    private final static int x=0;

    private static C c=new C();

    public static void main(String[] args) {
        test6();
    }

    /**
     * 线程安全方式六：最后想的一种方式就是加锁，显示锁
     */
    private static void test7(){
    }


    /**
     * 线程安全方式五：ThreadLocal 例子参考ThreadLocalTest类
     */
    private static void test6(){
    }

    /**
     * 线程安全方式四：安全的发布，不要把引用类型返回给调用者
     */
    private static void test5(){
        B b =new B();
        b.getList();//不安全的发布
        b.getList2();//安全的发布,返回的复制后的list
        b.judgeExist("A");//安全的发布

    }

    /**
     * 线程安全方式四：volatile修饰+cas或者 volatile修饰+synchronize
     * volatile只能保证内存的可见性，并不是线程安全的，最适合一个线程写多个线程读的情景，（使用sycn内置锁锁住后修改，要么使用cas操作来修改，
     * 这两种方式能够保证一个线程写多个线程读）
     */
    private static void test4(){

    }

    /**
     * 线程安全方式三：让类不可变;多线程访问的变量使用final修饰
     *
     * final修饰非引用类型
     * 非要修饰引用类型的话，引用类型的属性也要是final修饰的
     *
     * 如果不用final修饰，单纯不提供修改方法和有返回值是成员变量的，这些还是不够的，反射能够修改不加final的变量值（不太推荐，实在没办法才这样做）
     */
    private static void test3(){
        new ForThreads(){
            @Override
            protected void running() {
                System.out.println(x);
            }
        }.doWork(3);
    }

    /**
     * 线程安全方式二：类无状态，B类没有任何属性
     */
    private static void test2(){
        final D d=new D();
        new ForThreads(){
            @Override
            protected void running() {
                d.mm();
            }
        }.doWork(3);
    }

    /**
     * 线程安全方式一：栈封闭：线程在调用方法这，a变量的域就是仅限在方法的范围；并且方法没有入参也没有返回值。
     */
    private static void test1(){

        new ForThreads(){
            @Override
            protected void running() {
                m1();
            }
        }.doWork(3);
    }

    public static void m1(){
        int a=0;
    }

    private static class B{


        public void mm(){}
        public boolean judgeExist(String s){
            return list.contains(s);
        }
        public List<String> getList(){
            return list;
        }
        public List<String> getList2(){
            List<String> copy =new ArrayList<>();
            Collections.copy(copy,list);
            return copy;
        }

        private List<String> list =new ArrayList<>();

        public B(){
            list.add("A");
            list.add("B");
            list.add("C");
            list.add("D");
        }



    }
    private static class D{
        public void mm(){}
    }
    private static class C extends Thread{
    }


}
