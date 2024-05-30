package com.rkyao.springbootasynctask.biz.biz01.controller;

import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Req;
import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Resp;
import com.rkyao.springbootasynctask.biz.biz01.service.Biz01Service;
import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Req;
import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Resp;
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
@RequestMapping("/biz01")
public class Biz01Controller {

    @Autowired
    private Biz01Service biz01Service;

    @Autowired
    private TaskService<Biz01Req, Biz01Resp> taskService01;

    /**
     * localhost:8080/biz01/add?a=1&b=1
     *
     * @param req 参数
     * @return 结果
     */
    @RequestMapping("/add")
    public Biz01Resp add(Biz01Req req) {
        return biz01Service.add(req);
    }

    /**
     * localhost:8080/biz01/addAsync?a=2&b=2
     *
     * @param req 参数
     * @return 任务id
     */
    @RequestMapping("/addAsync")
    public String addAsync(Biz01Req req) {
        return taskService01.asyncExecute(req);
    }

    /**
     * 获取任务状态
     * localhost:8080/biz01/getStatus?taskId=e5f72167efaf4a2faf6e34119d583e61
     *
     * @param taskId 任务id
     * @return 状态
     */
    @RequestMapping("/getStatus")
    public TaskStatus getStatus(String taskId) {
        return taskService01.getStatus(taskId);
    }

    /**
     * 获取任务状态
     * localhost:8080/biz01/getStatusMap?taskIdListStr=e5f72167efaf4a2faf6e34119d583e61,e5f72167efaf4a2faf6e34119d583e61
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
        return taskService01.getStatusMap(taskIdList);
    }

    /**
     * 获取所有任务状态
     * localhost:8080/biz01/getAllStatus
     *
     * @return 状态
     */
    @RequestMapping("/getAllStatus")
    public Map<String, TaskStatus> getAllStatus() {
        return taskService01.getAllStatus();
    }

    /**
     * 获取任务执行结果
     * localhost:8080/biz01/getResult?taskId=e5f72167efaf4a2faf6e34119d583e61
     *
     * @param taskId 任务id
     * @return 结果
     */
    @RequestMapping("/getResult")
    public Biz01Resp getResult(String taskId) {
        return taskService01.getResult(taskId, Biz01Resp.class);
    }

    /**
     * 获取任务状态
     * localhost:8080/biz01/getResultMap?taskIdListStr=e5f72167efaf4a2faf6e34119d583e61,e5f72167efaf4a2faf6e34119d583e61
     *
     * @param taskIdListStr 任务id列表，多个逗号隔开
     * @return key: 任务id val: 任务结果
     */
    @RequestMapping("/getResultMap")
    public Map<String, Biz01Resp> getResultMap(String taskIdListStr) {
        if (StringUtils.isEmpty(taskIdListStr)) {
            return new HashMap<>();
        }
        List<String> taskIdList = Arrays.asList(taskIdListStr.split(","));
        return taskService01.getResultMap(taskIdList, Biz01Resp.class);
    }

}
