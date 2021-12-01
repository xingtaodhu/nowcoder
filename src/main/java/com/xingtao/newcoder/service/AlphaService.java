package com.xingtao.newcoder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "c.AlphaService")
public class AlphaService {
//    // 让该方法在多线程环境下,被异步的调用.
//    @Async
//    public void execute1() {
//        log.debug("execute1");
//    }
//
//    @Scheduled(initialDelay = 10000, fixedRate = 1000)
//    public void execute2() {
//        log.debug("execute2");
//    }
}
