package com.concurrent.practice.framework;

import com.concurrent.practice.framework.vo.TaskResult;

public interface ITaskProcesser<T,R> {

    TaskResult<R> taskExecute(T data);
}
