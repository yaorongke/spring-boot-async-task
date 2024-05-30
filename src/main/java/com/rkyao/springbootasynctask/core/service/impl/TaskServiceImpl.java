package com.rkyao.springbootasynctask.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rkyao.springbootasynctask.core.constant.TaskConstant;
import com.rkyao.springbootasynctask.core.enums.TaskStatus;
import com.rkyao.springbootasynctask.core.func.TaskCallback;
import com.rkyao.springbootasynctask.core.func.TaskExHandler;
import com.rkyao.springbootasynctask.core.func.TaskExecutor;
import com.rkyao.springbootasynctask.core.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 任务处理接口实现
 *
 * @author yaorongke
 * @date 2024/5/18
 */
@Slf4j
public class TaskServiceImpl<T, R> implements TaskService<T, R> {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final RedisTemplate redisTemplate;

    private final TaskExecutor<T, R> taskExecutor;

    private final TaskCallback<R> taskCallback;

    private final TaskExHandler<T> taskExceptionHandler;

    public TaskServiceImpl(ThreadPoolTaskExecutor threadPoolTaskExecutor, RedisTemplate redisTemplate, TaskExecutor<T, R> taskExecutor, TaskCallback<R> taskCallback, TaskExHandler<T> taskExceptionHandler) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.redisTemplate = redisTemplate;
        this.taskExecutor = taskExecutor;
        this.taskCallback = taskCallback;
        this.taskExceptionHandler = taskExceptionHandler;
    }

    @Override
    public String asyncExecute(T params) {
        // 生成任务id
        String taskId = generateTaskId();
        log.info("TaskServiceImpl::execute, taskId: {}, params: {}", taskId, params);

        // 更新任务状态 -> 排队中
        updateStatus(taskId, TaskStatus.PENDING);

        CompletableFuture.supplyAsync(() -> {
            // 更新任务状态 -> 执行中
            updateStatus(taskId, TaskStatus.IN_PROGRESS);
            return taskExecutor.execute(params);
        }, threadPoolTaskExecutor).whenComplete((result, throwable) -> {
            if (result != null && throwable == null) {
                // 保存执行结果
                saveResult(taskId, result);
                // 更新任务状态 -> 完成
                updateStatus(taskId, TaskStatus.COMPLETED);
            }

            if (taskCallback != null) {
                taskCallback.onComplete(result);
            }
        }).exceptionally(throwable -> {
            // 更新任务状态 -> 失败
            updateStatus(taskId, TaskStatus.FAILED);
            if (taskExceptionHandler != null) {
                taskExceptionHandler.onFailure(throwable);
            }
            return null;
        });
        return taskId;
    }

    @Override
    public void updateStatus(String taskId, TaskStatus status) {
        long start = System.currentTimeMillis();
        String redisKey = buildTaskStatusKey(taskId);
        redisTemplate.opsForValue().set(redisKey, status.name(), TaskConstant.TASK_EXPIRE_REDIS_KEY, TimeUnit.SECONDS);
        long cost = System.currentTimeMillis() - start;
        log.info("TaskServiceImpl::updateStatus -> taskId: {}, status: {}, cost: {} ms", taskId, status, cost);
    }

    @Override
    public TaskStatus getStatus(String taskId) {
        long start = System.currentTimeMillis();
        String redisKey = buildTaskStatusKey(taskId);
        Object obj = redisTemplate.opsForValue().get(redisKey);
        long cost = System.currentTimeMillis() - start;
        log.info("TaskServiceImpl::getStatus -> taskId: {}, status: {}, cost: {} ms", taskId, obj, cost);
        return obj == null ? null : TaskStatus.valueOf(obj.toString());
    }

    @Override
    public Map<String, TaskStatus> getStatusMap(List<String> taskIdList) {
        if (CollectionUtils.isEmpty(taskIdList)) {
            return new HashMap<>();
        }

        List<String> keyList = taskIdList.stream().map(this::buildTaskStatusKey).collect(Collectors.toList());

        List<String> statusList = redisTemplate.opsForValue().multiGet(keyList);
        if (CollectionUtils.isEmpty(statusList)) {
            return new HashMap<>();
        }

        Map<String, TaskStatus> statusMap = new LinkedHashMap<>();
        for (int i = 0; i < taskIdList.size(); i++) {
            String taskId = taskIdList.get(i);
            String status = statusList.get(i);
            TaskStatus taskStatus = StringUtils.isEmpty(status) ? null : TaskStatus.valueOf(status);
            statusMap.put(taskId, taskStatus);
        }
        return statusMap;
    }

    @Override
    public Map<String, TaskStatus> getAllStatus() {
        long start = System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys(String.format(TaskConstant.TASK_STATUS_REDIS_KEY, "*"));
        if (keys == null || keys.isEmpty()) {
            return new HashMap<>();
        }

        List<String> statusList = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(statusList)) {
            return new HashMap<>();
        }

        List<String> keyList = new ArrayList<>(keys);
        Map<String, TaskStatus> statusMap = new LinkedHashMap<>();
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            String status = statusList.get(i);
            TaskStatus taskStatus = StringUtils.isEmpty(status) ? null : TaskStatus.valueOf(status);
            statusMap.put(key, taskStatus);
        }

        long cost = System.currentTimeMillis() - start;
        log.info("TaskServiceImpl::getAllStatus -> statusMap: {}, cost: {} ms", statusMap, cost);
        return statusMap;
    }

    @Override
    public void saveResult(String taskId, Object result) {
        long start = System.currentTimeMillis();
        String redisKey = buildTaskResultKey(taskId);
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(result), TaskConstant.TASK_EXPIRE_REDIS_KEY, TimeUnit.SECONDS);
        long cost = System.currentTimeMillis() - start;
        log.info("TaskServiceImpl::saveResult -> taskId: {}, result: {}, cost: {} ms", taskId, result, cost);
    }

    @Override
    public R getResult(String taskId, Class<R> clazz) {
        long start = System.currentTimeMillis();
        String redisKey = buildTaskResultKey(taskId);
        Object result = redisTemplate.opsForValue().get(redisKey);
        long cost = System.currentTimeMillis() - start;
        log.info("TaskServiceImpl::getResult -> taskId: {}, result: {}, cost: {} ms", taskId, result, cost);
        if (result == null) {
            return null;
        }
        return JSONObject.parseObject(result.toString(), clazz);
    }

    @Override
    public Map<String, R> getResultMap(List<String> taskIdList, Class<R> clazz) {
        if (CollectionUtils.isEmpty(taskIdList)) {
            return new HashMap<>();
        }

        long start = System.currentTimeMillis();

        List<String> keyList = taskIdList.stream().map(this::buildTaskResultKey).collect(Collectors.toList());

        List<String> respList = redisTemplate.opsForValue().multiGet(keyList);
        if (CollectionUtils.isEmpty(respList)) {
            return new HashMap<>();
        }

        Map<String, R> resultMap = new LinkedHashMap<>();
        for (int i = 0; i < taskIdList.size(); i++) {
            String taskId = taskIdList.get(i);
            String result = respList.get(i);
            resultMap.put(taskId, JSONObject.parseObject(result, clazz));
        }
        long cost = System.currentTimeMillis() - start;
        log.info("TaskServiceImpl::getResultMap -> taskIdList: {}, resultMap: {}, cost: {} ms", taskIdList, resultMap, cost);
        return resultMap;
    }

    /**
     * 构建任务状态redis key
     *
     * @param taskId 任务id
     * @return redis key
     */
    private String buildTaskStatusKey(String taskId) {
        return String.format(TaskConstant.TASK_STATUS_REDIS_KEY, taskId);
    }

    /**
     * 构建任务结果redis key
     *
     * @param taskId 任务id
     * @return redis key
     */
    private String buildTaskResultKey(String taskId) {
        return String.format(TaskConstant.TASK_RESULT_REDIS_KEY, taskId);
    }

    /**
     * 生成任务id
     *
     * @return uuid
     */
    private String generateTaskId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
