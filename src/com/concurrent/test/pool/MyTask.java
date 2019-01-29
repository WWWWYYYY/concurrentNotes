package com.concurrent.test.pool;

import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.Random;
import java.util.concurrent.Callable;

public class MyTask {

    public static class MyRunnableTask implements Runnable {

        public int sleepTime=1000;

        public boolean isNeedSleep = false;

        public String taskName;
        public boolean isRandom =false;
        public MyRunnableTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {

            if (isRandom){
                sleepTime=(int) Math.random()*5000;
            }
            if (isNeedSleep) {
                ThreadSleepTool.sleep(sleepTime);
            }
            PrintUtils.log(taskName + " doWork done");

        }
    }

    public static class MyCallableTask<T> implements Callable<T> {

        public boolean isRandom =false;
        public int sleepTime=1000;

        public boolean isNeedSleep=false;
        public String taskName;
        private Random r= new Random();
        public MyCallableTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public T call() throws Exception {

            if (isRandom){
                sleepTime=r.nextInt(10)*1000;
            }
            if (isNeedSleep) {
                ThreadSleepTool.sleep(sleepTime);
            }
            PrintUtils.log(taskName + " doWork done sleepTime:"+sleepTime);
            return (T) taskName;
        }
    }

}
