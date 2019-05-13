package com.concurrent.practice.framework;

import com.concurrent.practice.framework.vo.JobInfo;
import com.concurrent.practice.framework.vo.TaskResult;

import java.util.List;
import java.util.concurrent.*;

/**
 * 作为多线程的框架：1、封装一个具有处理任务能力的线程池工具类PendingJobPool；
 * 其中包含的线程池 taskExecutor 线程数要是固定的，使用的是有界队列
 * 并且在PendingJobPool中要有jobInfoMap任务上下文，方便查询
 * <p>
 * 为了不让开发者重复创建PendingJobPool实例导致线程数飙升。则应该使用单例模式：类加载模式
 */
public class PendingJobPool {
    //必须使用有界；防止任务量过载导致服务器崩溃
    private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(5000);
    private static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 执行task的线程池
     */
    private static ExecutorService taskExecutor = new ThreadPoolExecutor(CPU_COUNT, CPU_COUNT, 0L, TimeUnit.SECONDS, queue);

    /**
     * jobInfo的上线文
     */
    private static ConcurrentHashMap<String, JobInfo<?>> jobInfoMap = new ConcurrentHashMap<>();

    /**
     * 完成任务后的处理器
     */
    private static CheckJobProcess checkJobProcess = CheckJobProcess.getInstance();


    //单例模式：类加载模式
    private PendingJobPool() {
    }

    private static class PendingJobPoolHolder {
        public static PendingJobPool pendingJobPool = new PendingJobPool();
    }

    public static PendingJobPool getInstance() {
        return PendingJobPoolHolder.pendingJobPool;
    }

    /**
     * job注册
     */
    public <R> boolean resisterJob(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser, long expireTime) {
        JobInfo<R> jobInfo = new JobInfo<>(jobName, jobLength, taskProcesser, expireTime);
        //putIfAbsent判断map中是否已经存在，不存在就put，已经存在就get出来
        if (jobInfoMap.putIfAbsent(jobName, jobInfo) != null) {
            throw new RuntimeException(jobName + "以及注册了！");
        }
        return true;
    }

    /**
     *  客户端在调用putTask过程中，框架只知道jobLength，所以putTask调用次数是否是jobLength的次数，由开发人员把控
     *  毕竟这个框架不是给第三方用的，是给内部开发人员使用。所以该方法的调用次数控制如果没有达到jobLength，那就是调用时除了问题
     *
     *  并且如果任务长度为10000也应该一次性将一个list.size=10000的list传入。这样也不合理；所有最优的方式还是一个一个的接收任务
     */
    public <T, R> boolean putTask(String jobName, T data) {
        JobInfo<R> jobInfo = getJob(jobName);
        PendingTask<T, R> task = new PendingTask<>(jobInfo, data);
        taskExecutor.execute(task);
        return true;
    }

    public boolean removeJob(String jobName) {
        jobInfoMap.remove(jobName);
        return true;
    }

    private <R> JobInfo<R> getJob(String jobName) {
        JobInfo<R> jobInfo = (JobInfo<R>) jobInfoMap.get(jobName);
        if (jobInfo == null) {
            throw new RuntimeException(jobName + "任务不存在！");
        }
        return jobInfo;
    }

    public <R> List<TaskResult<R>> getTaskDetail(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTaskDetails();
    }

    public <R> String getTaskProcess(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTotalTaskProcesser();
    }


    private static class PendingTask<T, R> implements Runnable {
        private JobInfo<R> jobInfo;
        private T processData;

        public PendingTask(JobInfo<R> jobInfo, T processData) {
            this.jobInfo = jobInfo;
            this.processData = processData;
        }

        @Override
        public void run() {
            ITaskProcesser<T, R> processer = (ITaskProcesser<T, R>) jobInfo.getTaskProcesser();
            TaskResult<R> result = null;

            try {
                result = processer.taskExecute(processData);

                if (result == null) {
                    result = new TaskResult<R>(TaskResultType.EXCEPTION, null, "result is null");
                }
                if (result.getResultType() == null) {
                    if (result.getReson() == null) {
                        result = new TaskResult<R>(TaskResultType.EXCEPTION, null, "reason is null");
                    } else {
                        result = new TaskResult<R>(TaskResultType.EXCEPTION, null, "reason is [" + result.getReson() + "]");
                    }
                }

            } catch (Exception e) {
                result = new TaskResult<R>(TaskResultType.EXCEPTION, null, e.getMessage());
            } finally {
                //1、把结果放入jobinfo的结果链表中
                //2、判断是否已经完成了整个job任务，完成了则需要加入到延迟队列中。
                jobInfo.addTaskResult(result,checkJobProcess);
//                if (jobInfo.isCompleted()) {//如果完成了则加入到延迟队列中，超过expireTime则从jobinfoMap中移除;checkJobProcess作用就是移除jobinfoMap中的元素
//                    checkJobProcess.putJob(jobInfo.getJobName(), jobInfo.getExpireTime());
//                }

            }

        }
    }
}
