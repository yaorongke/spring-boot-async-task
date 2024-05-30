package com.rkyao.springbootasynctask.biz.biz02.service;

import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Req;
import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Resp;

/**
 * 业务测试 service
 *
 * @author yaorongke
 * @date 2024/5/18
 */
public interface Biz02Service {

    /**
     * 任务执行方法 乘法计算
     *
     * @param req 参数
     * @return 计算结果
     */
    Biz02Resp multi(Biz02Req req);

    /**
     * 任务回调方法
     *
     * @param resp 计算结果
     */
    void printResult(Biz02Resp resp);

    /**
     * 任务异常处理方法
     *
     * @param t   异常信息
     */
    void printEx(Throwable t);

}
