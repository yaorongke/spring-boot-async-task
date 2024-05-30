package com.rkyao.springbootasynctask.biz.biz02.service.impl;

import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Req;
import com.rkyao.springbootasynctask.biz.biz02.dto.Biz02Resp;
import com.rkyao.springbootasynctask.biz.biz02.service.Biz02Service;
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
public class Biz02ServiceImpl implements Biz02Service {

    @Override
    public Biz02Resp multi(Biz02Req req) {
        log.info("Biz02ServiceImpl::multi -> " + req.getA() + ", " + req.getB());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (req.getA() == 1) {
            throw new RuntimeException("error");
        }

        Integer multi = req.getA() * req.getB();
        Biz02Resp resp = new Biz02Resp();
        resp.setMulti(multi);
        return resp;
    }

    @Override
    public void printResult(Biz02Resp resp) {
        log.info("Biz02ServiceImpl::printResult -> " + resp.getMulti());
    }

    @Override
    public void printEx(Throwable t) {
        log.error("Biz02ServiceImpl::printEx -> " + t.getMessage());
    }

}
