package com.concurrent.practice.client;

import com.concurrent.practice.client.vo.ExcelVo;
import com.concurrent.practice.framework.PendingJobPool;
import com.concurrent.practice.framework.vo.TaskResult;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

import java.util.LinkedList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        PendingJobPool pool =PendingJobPool.getInstance();

        String jobName ="job1";
        int jobLength=20;
        pool.resisterJob(jobName,jobLength,new TaskProcesser(),1000*10);
        for (int i=0;i<jobLength;i++){
            pool.putTask(jobName,new ExcelVo("主题"+i,i));
        }
        List<TaskResult<String>> list =new LinkedList<>();
        while (true){
            List<TaskResult<String>> taskDetail = pool.getTaskDetail(jobName);
            list.addAll(taskDetail);

            PrintUtils.log(pool.getTaskProcess(jobName));
            if (list.size()==jobLength){
                PrintUtils.log(jobName+"任务结束");
                break;
            }
            ThreadSleepTool.sleep(1000);//每秒查询一次
        }


    }
}
