package com.rkyao.springbootasynctask.biz.biz01.config;

import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Req;
import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Resp;
import com.rkyao.springbootasynctask.biz.biz01.service.Biz01Service;
import com.rkyao.springbootasynctask.core.func.TaskCallback;
import com.rkyao.springbootasynctask.core.func.TaskExHandler;
import com.rkyao.springbootasynctask.core.func.TaskExecutor;
import com.rkyao.springbootasynctask.core.service.TaskService;
import com.rkyao.springbootasynctask.core.service.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 业务逻辑配置注入
 *
 * @author yaorongke
 * @date 2024/5/25
 */
@Configuration
public class TaskService01Config {

    @Autowired
    private Biz01Service biz01Service;

    @Bean
    public TaskService<Biz01Req, Biz01Resp> taskService01(ThreadPoolTaskExecutor bizTaskExecutor, RedisTemplate<String, Object> redisTemplate) {
        TaskExecutor<Biz01Req, Biz01Resp> taskExecutor = biz01Service::add;
        TaskCallback<Biz01Resp> taskCallback = biz01Service::printResult;
        TaskExHandler<Biz01Req> taskExceptionHandler = biz01Service::printEx;
        return new TaskServiceImpl<>(bizTaskExecutor, redisTemplate, taskExecutor, taskCallback, taskExceptionHandler);
    }

}
