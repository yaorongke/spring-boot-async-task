package com.rkyao.springbootasynctask.core.constant;

/**
 * 任务执行常量类
 *
 * @author yaorongke
 * @date 2024/5/18
 */
public class TaskConstant {

    /**
     * 任务状态 redis key
     */
    public static final String TASK_STATUS_REDIS_KEY = "task_status:%s";

    /**
     * 任务结果 redis key
     */
    public static final String TASK_RESULT_REDIS_KEY = "task_result:%s";

    /**
     * 任务超时时间 redis key
     * 一小时
     */
    public static final int TASK_EXPIRE_REDIS_KEY = 3600;

}
