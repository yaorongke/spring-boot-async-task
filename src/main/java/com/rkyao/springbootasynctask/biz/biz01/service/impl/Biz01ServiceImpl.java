package com.rkyao.springbootasynctask.biz.biz01.service.impl;

import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Req;
import com.rkyao.springbootasynctask.biz.biz01.dto.Biz01Resp;
import com.rkyao.springbootasynctask.biz.biz01.service.Biz01Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 业务测试 接口实现
 *
 * @author yaorongke
 * @date 2024/5/18
 */
@Service
@Slf4j
public class Biz01ServiceImpl implements Biz01Service {

    @Override
    public Biz01Resp add(Biz01Req req) {
        log.info("BizServiceImpl::add -> " + req.getA() + ", " + req.getB());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (req.getA() == 1) {
            throw new RuntimeException("error");
        }

        Integer count = req.getA() + req.getB();
        Biz01Resp resp = new Biz01Resp();
        resp.setCount(count);
        return resp;
    }

    @Override
    public void printResult(Biz01Resp resp) {
        log.info("BizServiceImpl::printResult -> " + resp.getCount());
    }

    @Override
    public void printEx(Throwable t) {
        log.error("BizServiceImpl::printEx -> " + t.getMessage());
    }

}
