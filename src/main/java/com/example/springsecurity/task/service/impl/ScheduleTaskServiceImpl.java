package com.example.springsecurity.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.task.dao.ScheduleTaskDao;
import com.example.springsecurity.task.entity.ScheduleTask;
import com.example.springsecurity.task.service.ScheduleTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * (ScheduleTask)表服务实现类
 *
 * @author makejava
 * @since 2023-04-02 19:22:21
 */
@Service("scheduleTaskService")
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskDao, ScheduleTask> implements ScheduleTaskService {

    @Resource
    ScheduleTaskDao scheduleTaskDao;

    @Override
    public Ret getTaskList(Page page, Map<String, Object> param) {
        QueryWrapper<ScheduleTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("bean_name",param.get("beanName"));
        queryWrapper.like("method_name",param.get("methodName"));
        queryWrapper.orderByAsc("create_time");
        Page dataPage = scheduleTaskDao.selectPage(page, queryWrapper);
        return Ret.getPageOkResult(dataPage);
    }
}

