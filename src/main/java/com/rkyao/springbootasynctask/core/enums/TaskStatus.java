package com.rkyao.springbootasynctask.core.enums;

/**
 * 任务执行状态
 *
 * @author yaorongke
 * @date 2024/5/18
 */
public enum TaskStatus {

    /**
     * 排队中
     */
    PENDING,

    /**
     * 执行中
     */
    IN_PROGRESS,

    /**
     * 完成
     */
    COMPLETED,

    /**
     * 失败
     */
    FAILED

}
