package com.example.springsecurity.task.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

//定时任务测试
/**
 * beanName: taskDemo 添加表任务表
 */
@Slf4j
@Component("taskDemo")
public class Task1 {

    /**
     * 测试有参数的任务执行
     * @param params
     */
    public void taskByParams(String params) {
        log.info("taskByParams执行时间:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        log.info("taskByParams执行有参示例任务：{}",params);
    }

    /***
     * 测试无参任务执行
     */
    public void taskNoParams() {
        log.info("taskByParams执行时间:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        log.info("taskNoParams执行无参示例任务");
    }
 
    public void test(String params) {
        log.info("test执行时间:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        log.info("test执行有参示例任务：{}",params);
    }
} 