package com.rkyao.springbootasynctask.biz.biz01.service;

import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Req;
import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Resp;

/**
 * 业务测试 service
 *
 * @author yaorongke
 * @date 2024/5/18
 */
public interface Biz01Service {

    /**
     * 任务执行方法 加法计算
     *
     * @param req 参数
     * @return 计算结果
     */
    Biz01Resp add(Biz01Req req);

    /**
     * 任务回调方法
     *
     * @param resp 计算结果
     */
    void printResult(Biz01Resp resp);

    /**
     * 任务异常处理方法
     *
     * @param t   异常信息
     */
    void printEx(Throwable t);

}
