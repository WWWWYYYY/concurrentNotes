package com.concurrent.test.single;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

/**
 * 懒汉式单例
 */
public class SingleInstanceTest {

    //如果没有用 volatile 修饰，则第二个线程在调用getInstance方法时不能明确知道User对象是否实例化完成。所以在调用user对象里的name属性时发生空指针异常
    private static volatile SingleInstanceTest instance;
    private User user;
    public static class User{
        public String name="123";
        public User(){
            ThreadSleepTool.sleep(1000);//初始化耗时比较长
        }
    }

    public User getUser() {
        return user;
    }

    private SingleInstanceTest(){
        user=new User();
    }

    public static SingleInstanceTest getInstance(){
        if (instance==null){
            synchronized (SingleInstanceTest.class){
                if (instance==null)
                instance=new SingleInstanceTest();
            }
        }
        return instance;
    }



    public static void main(String[] args) {
        test1();
    }

    private static void test1(){
        new ForThreads(){
            @Override
            protected void running() {

                PrintUtils.log("name:"+SingleInstanceTest.getInstance().user.name);
            }
        }.doWork(2);
    }
}
