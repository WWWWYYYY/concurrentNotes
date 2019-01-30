package com.concurrent.practice.client;

import com.concurrent.practice.client.vo.ExcelVo;
import com.concurrent.practice.framework.ITaskProcesser;
import com.concurrent.practice.framework.TaskResultType;
import com.concurrent.practice.framework.vo.TaskResult;
import com.concurrent.utils.PrintUtils;
import com.concurrent.utils.ThreadSleepTool;

/**
 * 业务处理器
 */
public class TaskProcesser implements ITaskProcesser<ExcelVo,String> {
    @Override
    public TaskResult<String> taskExecute(ExcelVo data) {
        PrintUtils.log(data.getTheme()+" task is running");

        TaskResult<String> result =null;
        if (data.getIndex()%3==0){
            result =new TaskResult<>(TaskResultType.FAIL,data.getTheme(),"index is %3==0");
        }else
        if (data.getIndex()%4==0){
            result =new TaskResult<>(TaskResultType.EXCEPTION,data.getTheme(),"index is %4==0");
        }else {
            result =new TaskResult<>(data.getTheme());
        }

        ThreadSleepTool.sleep(2000);
        return result;
    }
}
