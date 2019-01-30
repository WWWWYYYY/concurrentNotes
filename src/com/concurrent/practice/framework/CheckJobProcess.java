package com.concurrent.practice.framework;


import com.concurrent.practice.framework.vo.DelayVo;
import com.concurrent.utils.PrintUtils;

import java.util.concurrent.DelayQueue;

/**
 * 任务完成以后，在一定时间内仍然提供查询功能，但在之后要清理掉，定期处理过期任务
 */
public class CheckJobProcess {

    private static DelayQueue<DelayVo<String>> queue =new DelayQueue<>();

    private CheckJobProcess(){}

    private static class CheckJobProcessHolder{
            public static CheckJobProcess checkJobProcess=new CheckJobProcess();
    }
    public static CheckJobProcess getInstance(){
        return CheckJobProcessHolder.checkJobProcess;
    }

    private static class FetchJob implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    DelayVo<String> vo = queue.take();
                    PendingJobPool.getInstance().removeJob(vo.getData());

                    PrintUtils.log(vo.getData()+"从jobInfoMap移除");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 思路:当任务完成后，将jobName包装成DelayVo放到queue中，等到时间到了FetchJob中queue.take()获取到这个任务名后移除{@link PendingJobPool#jobInfoMap}中的元素
     *  在移除之前，都可以查询的到当前任务的详情
     */
    public boolean putJob(String jobName,long activeTime){
        DelayVo<String> vo =new DelayVo<>(activeTime,jobName);
        return  queue.offer(vo);
    }

    static {
        Thread t =new Thread(new FetchJob(), "JobInfo Clean Thread ");
        t.setDaemon(true);
        t.start();
        PrintUtils.log("JobInfo Clean Thread running");
    }

}
