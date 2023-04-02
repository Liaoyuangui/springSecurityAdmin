package com.example.springsecurity.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.task.entity.ScheduleTask;

import java.util.Map;

/**
 * (ScheduleTask)表服务接口
 *
 * @author makejava
 * @since 2023-04-02 19:22:19
 */
public interface ScheduleTaskService extends IService<ScheduleTask> {

    /***
     * 查询任务列表
     * @param page
     * @param param
     * @return
     */
    Ret getTaskList(Page page, Map<String, Object> param);
}

