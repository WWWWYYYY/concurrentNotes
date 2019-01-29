package com.concurrent.test.pool;

public class MyThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        MyThreadPool pool =new MyThreadPool();
        pool.execute(new MyThreadPool.MyWork("aaa"));
        pool.execute(new MyThreadPool.MyWork("bbb"));
        pool.execute(new MyThreadPool.MyWork("ccc"));
        pool.execute(new MyThreadPool.MyWork("ddd"));
                Thread.sleep(3000);
        pool.destory();


        }
}
