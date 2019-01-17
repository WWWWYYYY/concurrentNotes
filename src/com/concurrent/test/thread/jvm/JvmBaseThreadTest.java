package com.concurrent.test.thread.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * jvm 最基本的几个守护线程
 */
public class JvmBaseThreadTest {

    public static void main(String[] args) {
        //虚拟机线程管理的接口
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos =
                threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "]" + " "
                    + threadInfo.getThreadName());
        }
    }
}
