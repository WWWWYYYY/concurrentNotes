package com.concurrent.practice.framework.vo;

import com.concurrent.practice.framework.CheckJobProcess;
import com.concurrent.practice.framework.ITaskProcesser;
import com.concurrent.practice.framework.TaskResultType;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class JobInfo<R> {
    //工作的唯一标识
    private final String jobName;
    /**
     * 任务长度
     */
    private final int jobLength;

    private final ITaskProcesser<?,?> taskProcesser;
    /**
     * 任务成功个数
     */
    private AtomicInteger successCount;
    /**
     * 已处理的任务个数
     */
    private AtomicInteger taskProcesserCount;
    /**
     * 每一个任务的详情
     */
    private LinkedBlockingDeque<TaskResult<R>> taskDetailQueue;
    private final long expireTime ;

    public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser, long expireTime) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcesser = taskProcesser;
        this.successCount = new AtomicInteger(0);
        this.taskProcesserCount = new AtomicInteger(0);
        this.taskDetailQueue = new LinkedBlockingDeque<>(jobLength);
        this.expireTime = expireTime;
    }


    public int getSuccessCount() {
        return successCount.get();
    }

    public int getTaskProcesserCount() {
        return taskProcesserCount.get();
    }
    public int getFailCount() {
        return taskProcesserCount.get()-successCount.get();
    }
    public String getTotalTaskProcesser() {
        return "success["+successCount.get()+"]/current["+taskProcesserCount.get()+"] total["+jobLength+"]";
    }
    public List<TaskResult<R>> getTaskDetails(){
        List<TaskResult<R>> list = new LinkedList<>();//在这个方法中这个集合一直再做增加的操作，所以使用linkedlist
        TaskResult<R> taskResult;
        while ((taskResult=taskDetailQueue.pollFirst())!=null){
            list.add(taskResult);
        }
        return list;
    }

    /**
     * 在向jobinfo添加task结果的同时判断是否job已经完成
     * @param taskResult
     * @param checkJobProcess 作用就是移除jobinfoMap中的元素
     * @return
     */
    public boolean addTaskResult(TaskResult<R> taskResult,CheckJobProcess checkJobProcess){
        if (TaskResultType.SUCCESS.equals(taskResult.getResultType())){
            successCount.incrementAndGet();
        }
        //并发下 存在taskProcesserCount和taskDetailQueue数量不一致，从业务角度上来看，不关注这两个数据的强一致性，只要求最终一致性，所以不加锁（根据业务的性质来判断是否需要强一致性）
        taskDetailQueue.addLast(taskResult);
//        synchronized (this){//必须让taskProcesserCount++操作和判断是否完成的操作 变为一个原子操作 ；如果需要强一致性需要打开synchronized
            taskProcesserCount.incrementAndGet();
            if (isCompleted()) {//如果完成了则加入到延迟队列中，超过expireTime则从jobinfoMap中移除
                checkJobProcess.putJob(jobName, expireTime);
            }
//        }

        return true;
    }

    public ITaskProcesser<?, ?> getTaskProcesser() {
        return taskProcesser;
    }

    public String getJobName() {
        return jobName;
    }

    public int getJobLength() {
        return jobLength;
    }


    public boolean isCompleted(){
        if (jobLength==taskProcesserCount.get())return true;
        return false;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
