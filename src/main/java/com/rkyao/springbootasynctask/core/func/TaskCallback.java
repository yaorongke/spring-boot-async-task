package com.rkyao.springbootasynctask.core.func;

/**
 * 任务回调 函数式接口
 *
 * @author yaorongke
 * @date 2024/5/18
 */
@FunctionalInterface
public interface TaskCallback<R> {

    /**
     * 任务执行完毕后的回调方法
     *
     * @param result 执行结果
     */
    void onComplete(R result);

}
