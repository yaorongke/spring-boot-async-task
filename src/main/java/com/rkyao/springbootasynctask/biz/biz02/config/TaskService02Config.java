package com.rkyao.springbootasynctask.biz.biz02.config;

import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Req;
import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Resp;
import com.rkyao.springbootasynctask.biz.biz02.service.Biz02Service;
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
public class TaskService02Config {

    @Autowired
    private Biz02Service biz02Service;

    @Bean
    public TaskService<Biz02Req, Biz02Resp> taskService02(ThreadPoolTaskExecutor bizTaskExecutor, RedisTemplate<String, Object> redisTemplate) {
        TaskExecutor<Biz02Req, Biz02Resp> taskExecutor = biz02Service::multi;
        TaskCallback<Biz02Resp> taskCallback = biz02Service::printResult;
        TaskExHandler<Biz02Req> taskExceptionHandler = biz02Service::printEx;
        return new TaskServiceImpl<>(bizTaskExecutor, redisTemplate, taskExecutor, taskCallback, taskExceptionHandler);
    }

}
