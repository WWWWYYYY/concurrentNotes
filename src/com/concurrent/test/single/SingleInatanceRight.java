package com.concurrent.test.single;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类懒加载模式：适用于单例设计；并且如果这个实例下有很多属性，一开始又用不上这些属性（这些属性又都是引用类型）就没有必要在实例用单例的时候同时初始化这些引用类型的属性；
 * 如果每个引用类型的属性的初始化都比较消耗时间。初始化单例的时候就全部初始化全部属性就会消耗特别多的时间(这是个不好的现象)
 *
 * 类加载模式：利用jvm在加载类时会进行加锁，只会有一个线程进行类加载，其他线程只会阻塞等待其他现在把类加载完毕
 *
 * 相比饿汉式和普通懒汉式 {@link SingleInstanceTest} ps：枚举也是饿汉式的
 * 类加载模式才是最优的选择（唯一缺陷就是有一个引用型属性就要多一个内部类，基本可以忽略这个缺点）
 */
public class SingleInatanceRight {
    private SingleInatanceRight() {
    }

    private static SingleInatanceRight instance;

    private static class InnerInit {
        public static SingleInatanceRight ins = new SingleInatanceRight();
    }

    public static SingleInatanceRight getInstance() {
        return InnerInit.ins;
    }

    private static boolean judgeExistClazz(String fullClazzName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class cl = Class.forName("java.lang.ClassLoader", false, Thread.currentThread().getContextClassLoader());
        Method method = cl.getDeclaredMethod("findLoadedClass", new Class[]{String.class});
        method.setAccessible(true);
        boolean flag = true;
        if (method.invoke(Thread.currentThread().getContextClassLoader(), fullClazzName) != null) {
            System.out.println(fullClazzName + "已经加载!");
            flag = true;
        } else {
            System.out.println(fullClazzName + "尚未加载!");
            flag = false;
        }
        return flag;

    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        test1();
    }

    //

    /**
     * 测试内部类加载时机；是否会跟外部类一起加载到jvm中：结论是不会
     *
     * 结果：
     * com.concurrent.test.single.SingleInatanceRight$InnerInit尚未加载!
     * com.concurrent.test.single.SingleInatanceRight已经加载!
     * com.concurrent.test.single.SingleInatanceRight$InnerInit尚未加载!
     * com.concurrent.test.single.SingleInatanceRight$InnerInit已经加载!
     */
    private static void test1() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        judgeExistClazz("com.concurrent.test.single.SingleInatanceRight$InnerInit");
        Class.forName("com.concurrent.test.single.SingleInatanceRight");
        judgeExistClazz("com.concurrent.test.single.SingleInatanceRight");
        judgeExistClazz("com.concurrent.test.single.SingleInatanceRight$InnerInit");
        SingleInatanceRight.getInstance();
        judgeExistClazz("com.concurrent.test.single.SingleInatanceRight$InnerInit");
    }
}
