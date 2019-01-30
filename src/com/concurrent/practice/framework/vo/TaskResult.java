package com.concurrent.practice.framework.vo;

import com.concurrent.practice.framework.TaskResultType;

public class TaskResult<R> {

    private final TaskResultType resultType;

    private final R returnValue;

    private final String reson;

    public TaskResult(TaskResultType resultType, R returnValue, String reson) {
        this.resultType = resultType;
        this.returnValue = returnValue;
        this.reson = reson;
    }

    public TaskResult(R returnValue) {
        this.returnValue = returnValue;
        this.reson="SUCCESS";
        this.resultType=TaskResultType.SUCCESS;
    }

    public TaskResultType getResultType() {
        return resultType;
    }

    public R getReturnValue() {
        return returnValue;
    }

    public String getReson() {
        return reson;
    }
}
