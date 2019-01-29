package com.concurrent.test.content;

import com.concurrent.utils.ForThreads;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue不存放元素 所以不论是先put还是take 都会被阻塞 直到有一对put/take出现
 * 一般用在任务类型比较紧急的情况下，希望任务马上放马上拿
 */
public class SynchronousQueueTest {

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    private static void test1() throws InterruptedException {
       final SynchronousQueue<String> queue =new SynchronousQueue<>();
        new ForThreads(){
            @Override
            protected void running() {
                try {
                    ThreadSleepTool.sleep(1000);
                    PrintUtils.log(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.doWork(1);
        queue.put("data");
    }
}
