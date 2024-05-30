package com.rkyao.springbootasynctask.biz.biz02.controller;

import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Resp;
import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Req;
import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Resp;
import com.rkyao.springbootasynctask.biz.biz02.service.Biz02Service;
import com.rkyao.springbootasynctask.core.enums.TaskStatus;
import com.rkyao.springbootasynctask.core.func.TaskCallback;
import com.rkyao.springbootasynctask.core.func.TaskExHandler;
import com.rkyao.springbootasynctask.core.func.TaskExecutor;
import com.rkyao.springbootasynctask.core.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务测试 controller
 *
 * @author yaorongke
 * @date 2024/5/18
 */
@RestController
@RequestMapping("/biz02")
public class Biz02Controller {

    @Autowired
    private Biz02Service biz02Service;

    @Autowired
    private TaskService<Biz02Req, Biz02Resp> taskService02;

    /**
     * localhost:8080/biz02/multi?a=2&b=3
     *
     * @param req 参数
     * @return 结果
     */
    @RequestMapping("/multi")
    public Biz02Resp multi(Biz02Req req) {
        return biz02Service.multi(req);
    }

    /**
     * localhost:8080/biz02/multiAsync?a=2&b=3
     *
     * @param req 参数
     * @return 任务id
     */
    @RequestMapping("/multiAsync")
    public String multiAsync(Biz02Req req) {
        return taskService02.asyncExecute(req);
    }

    /**
     * 获取任务状态
     * localhost:8080/biz02/getStatus?taskId=e5f72167efaf4a2faf6e34119d583e61
     *
     * @param taskId 任务id
     * @return 状态
     */
    @RequestMapping("/getStatus")
    public TaskStatus getStatus(String taskId) {
        return taskService02.getStatus(taskId);
    }


    /**
     * 获取任务状态
     * localhost:8080/biz02/getStatusMap?taskIdListStr=e5f72167efaf4a2faf6e34119d583e61,e5f72167efaf4a2faf6e34119d583e61
     *
     * @param taskIdListStr 任务id列表，多个逗号隔开
     * @return key: 任务id val: 任务状态 {@link TaskStatus}
     */
    @RequestMapping("/getStatusMap")
    public Map<String, TaskStatus> getStatusMap(String taskIdListStr) {
        if (StringUtils.isEmpty(taskIdListStr)) {
            return new HashMap<>();
        }
        List<String> taskIdList = Arrays.asList(taskIdListStr.split(","));
        return taskService02.getStatusMap(taskIdList);
    }

    /**
     * 获取所有任务状态
     * localhost:8080/biz02/getAllStatus
     *
     * @return 状态
     */
    @RequestMapping("/getAllStatus")
    public Map<String, TaskStatus> getAllStatus() {
        return taskService02.getAllStatus();
    }

    /**
     * 获取任务执行结果
     * localhost:8080/biz02/getResult?taskId=e5f72167efaf4a2faf6e34119d583e61
     *
     * @param taskId 任务id
     * @return 结果
     */
    @RequestMapping("/getResult")
    public Biz02Resp getResult(String taskId) {
        return taskService02.getResult(taskId, Biz02Resp.class);
    }

    /**
     * 获取任务状态
     * localhost:8080/biz02/getResultMap?taskIdListStr=e5f72167efaf4a2faf6e34119d583e61,e5f72167efaf4a2faf6e34119d583e61
     *
     * @param taskIdListStr 任务id列表，多个逗号隔开
     * @return key: 任务id val: 任务结果
     */
    @RequestMapping("/getResultMap")
    public Map<String, Biz02Resp> getResultMap(String taskIdListStr) {
        if (StringUtils.isEmpty(taskIdListStr)) {
            return new HashMap<>();
        }
        List<String> taskIdList = Arrays.asList(taskIdListStr.split(","));
        return taskService02.getResultMap(taskIdList, Biz02Resp.class);
    }

}
