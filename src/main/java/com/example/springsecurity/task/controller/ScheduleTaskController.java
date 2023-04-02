package com.example.springsecurity.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.task.entity.ScheduleTask;
import com.example.springsecurity.task.service.ScheduleTaskService;
import com.example.springsecurity.task.service.impl.CronTaskRegistrar;
import com.example.springsecurity.task.service.impl.SchedulingRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * (ScheduleTask)表控制层
 *
 * @author makejava
 * @since 2023-04-02 19:22:07
 */
@RestController
@RequestMapping("task/scheduleTask")
public class ScheduleTaskController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    /***
     * 视图
     * @return
     */
    @RequestMapping("/indexView")
    @PreAuthorize("@ss.hasPermi('task:scheduleTask:view')")
    public ModelAndView view(){
        return new ModelAndView("/pages/task/task_list");
    }

    /***
     * 查询菜单列表
     * @param param
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermi('task:scheduleTask:query')")
    public Ret list(@RequestBody Map<String,Object> param){
        Page page = getPageParam(param);
        if(null == page){
            return pageError();
        }
        return scheduleTaskService.getTaskList(page,param);
    }

    /**
     * 添加定时任务
     *
     * @param sysJob
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('task:scheduleTask:add')")
    public Ret add(@RequestBody ScheduleTask sysJob) {
        sysJob.setCreateTime(new Date());
        sysJob.setUpdateTime(new Date());

        boolean insert = sysJob.insert();
        if (!insert) {
            return Ret.error("添加定时任务失败！");
        }else {
            if (sysJob.getJobStatus().equals(1)) {// 添加成功,并且状态是1，直接放入任务器
                SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
            }
        }
        return Ret.success("添加成功！");
    }

    /**
     * 修改定时任务
     *
     * @param sysJob
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('task:scheduleTask:update')")
    public Ret update(@RequestBody ScheduleTask sysJob) {
        sysJob.setCreateTime(new Date());
        sysJob.setUpdateTime(new Date());

        // 查询修改前任务
        ScheduleTask existedSysJob = new ScheduleTask();
        existedSysJob = existedSysJob.selectOne(new QueryWrapper<ScheduleTask>().eq("job_id", sysJob.getJobId()));
        // 修改任务
        boolean update = sysJob.update(new UpdateWrapper<ScheduleTask>().eq("job_id", sysJob.getJobId()));
        if (!update) {
            return Ret.error("修改失败！");
        } else {
            // 修改成功,则先删除任务器中的任务,并重新添加
            SchedulingRunnable task1 = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
            cronTaskRegistrar.removeCronTask(task1);
            if (sysJob.getJobStatus().equals(1)) {// 如果修改后的任务状态是1就加入任务器
                SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
            }
        }
        return Ret.success("修改成功！");
    }

    /**
     * 删除任务
     *
     * @param jobId 任务id
     * @return
     */
    @PostMapping("/del/{jobId}")
    @PreAuthorize("@ss.hasPermi('task:scheduleTask:delete')")
    public Ret del(@PathVariable("jobId") String jobId) {
        // 先查询要删除的任务信息
        ScheduleTask existedSysJob = new ScheduleTask();
        existedSysJob = existedSysJob.selectOne(new QueryWrapper<ScheduleTask>().eq("job_id", jobId));

        // 删除
        boolean del = existedSysJob.delete(new QueryWrapper<ScheduleTask>().eq("job_id", jobId));
        if (!del)
            return Ret.error("删除失败！");
        else {// 删除成功时要清除定时任务器中的对应任务
            SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
            cronTaskRegistrar.removeCronTask(task);
        }
        return Ret.success("删除成功！");
    }

    /***
     * 停止定时任务
     * @param jobId
     * @return
     */
    @PostMapping("/stop/{jobId}")
    @PreAuthorize("@ss.hasPermi('task:scheduleTask:stop')")
    public Ret stop(@PathVariable("jobId") String jobId){
        return changesStatus(jobId,0);
    }

    /***
     * 启动定时任务
     * @param jobId
     * @return
     */
    @PostMapping("/start/{jobId}")
    @PreAuthorize("@ss.hasPermi('task:scheduleTask:start')")
    public Ret start(@PathVariable("jobId") String jobId){
        return changesStatus(jobId,1);
    }

    /***
     * 停止/启动任务
     * @param jobId 任务id
     * @param status 0停止 1启动
     * @return
     */
    public Ret changesStatus(String jobId,Integer status) {
        // 修改任务状态
        ScheduleTask scheduleSetting = new ScheduleTask();
        scheduleSetting.setJobStatus(status);
        boolean job_id = scheduleSetting.update(new UpdateWrapper<ScheduleTask>().eq("job_id", jobId));
        if (!job_id) {
            return Ret.error("更新失败！");
        }
        // 查询修改后的任务信息
        ScheduleTask existedSysJob = new ScheduleTask();
        existedSysJob = existedSysJob.selectOne(new QueryWrapper<ScheduleTask>().eq("job_id", jobId));

        // 如果状态是1则添加任务
        String message = "";
        if (existedSysJob.getJobStatus().equals(1)) {
            message = "启动成功！";
            SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
            cronTaskRegistrar.addCronTask(task, existedSysJob.getCronExpression());
        } else {
            message = "停止成功！";
            // 否则清除任务
            SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
            cronTaskRegistrar.removeCronTask(task);
        }
        return Ret.success(message);
    }
}

