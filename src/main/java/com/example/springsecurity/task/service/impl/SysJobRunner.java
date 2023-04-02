package com.example.springsecurity.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.springsecurity.task.entity.ScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/***
 * 项目启动，加载数据库中的定时任务
 */
@Slf4j
@Service
public class SysJobRunner implements CommandLineRunner {
  
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;  
  
    @Override  
    public void run(String... args) {  
        // 初始加载数据库里状态为正常的定时任务  
        ScheduleTask existedSysJob = new ScheduleTask();
        List<ScheduleTask> jobList = existedSysJob.selectList(new QueryWrapper<ScheduleTask>().eq("job_status", 1));
        if (CollectionUtils.isNotEmpty(jobList)) {
            for (ScheduleTask job : jobList) {
                SchedulingRunnable task = new SchedulingRunnable(job.getBeanName(), job.getMethodName(), job.getMethodParams());  
                cronTaskRegistrar.addCronTask(task, job.getCronExpression());  
            }  
            log.info("定时任务已加载完毕...");
        }  else{
            System.out.println(">>>>>没有需要执行的任务<<<<<");
        }
    }  
} 