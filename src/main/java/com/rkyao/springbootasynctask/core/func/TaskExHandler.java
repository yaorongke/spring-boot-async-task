package com.rkyao.springbootasynctask.core.func;

/**
 * 任务异常处理 函数式接口
 *
 * @author yaorongke
 * @date 2024/5/19
 */
@FunctionalInterface
public interface TaskExHandler<T> {

    /**
     * 任务异常处理
     *
     * @param t      异常信息
     */
    void onFailure(Throwable t);

}
