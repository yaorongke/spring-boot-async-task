package com.rkyao.springbootasynctask.core.func;

/**
 * 任务执行 函数式接口
 *
 * @author yaorongke
 * @date 2024/5/19
 */
@FunctionalInterface
public interface TaskExecutor<T, R> {

    /**
     * 任务执行
     *
     * @param params 参数
     * @return 结果
     */
    R execute(T params);

}