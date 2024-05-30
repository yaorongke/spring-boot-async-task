package com.rkyao.springbootasynctask.core.service;

import com.rkyao.springbootasynctask.core.enums.TaskStatus;
import com.rkyao.springbootasynctask.core.func.TaskCallback;
import com.rkyao.springbootasynctask.core.func.TaskExHandler;
import com.rkyao.springbootasynctask.core.func.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Map;

/**
 * 任务处理接口
 *
 * @author yaorongke
 * @date 2024/5/18
 */
public interface TaskService<T, R> {

    /**
     * 提交任务 异步执行
     *
     * @param params                 任务参数
     * @return 任务id
     */
    String asyncExecute(T params);

    /**
     * 更新任务状态
     *
     * @param taskId 任务id
     * @param status 任务状态 {@link TaskStatus}
     */
    void updateStatus(String taskId, TaskStatus status);

    /**
     * 获取任务状态
     *
     * @param taskId 任务id
     * @return 任务状态 {@link TaskStatus}
     */
    TaskStatus getStatus(String taskId);

    /**
     * 获取任务状态 批量
     *
     * @param taskIdList 任务id列表
     * @return key: 任务id val: 任务状态 {@link TaskStatus}
     */
    Map<String, TaskStatus> getStatusMap(List<String> taskIdList);

    /**
     * 获取所有任务状态
     *
     * @return 任务状态列表 {@link TaskStatus}
     */
    Map<String, TaskStatus> getAllStatus();

    /**
     * 保存任务执行结果
     *
     * @param taskId 任务id
     * @param result 执行结果
     */
    void saveResult(String taskId, Object result);

    /**
     * 获取任务执行结果
     *
     * @param taskId 任务id
     * @return 执行结果
     */
    R getResult(String taskId, Class<R> clazz);

    /**
     * 获取任务结果 批量
     *
     * @param taskIdList 任务id列表
     * @return key: 任务id val: 任务结果
     */
    Map<String, R> getResultMap(List<String> taskIdList, Class<R> clazz);

}
