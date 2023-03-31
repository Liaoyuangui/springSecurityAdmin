# springSecurityAdmin 运行及说明

#项目技术栈
 springboot+springSecurity+mybatisplus+redis+JWT+vue2.js+elementui

#运行步骤
1.  把sql 文件夹下的sql文件导入本地 mysql 数据库。
    修改application.yml 文件中的datasource数据库连接，改成自己本地的库

2.  启动本地redis 

3.  启动项目

4.  访问登录页

   localhost:8080/toLogin
   
#登录用户
 sql文件中提供了3个用户，admin,ssman,root; 密码都是123456
 admin： 超级管理员无需任何授权就可以访问所有资源
 ssman:  授予了中层管理员角色，没有功能的删除权限，其余均有
 root:   授予了一般管理员角色，只拥有查询权限，没有操作权限
 没有操作权限的按钮在页面均不会显示，可自行创建用户和角色分配资源测试.
 
 #实现逻辑
 对需要授权的接口，前后端约定一个权限标识
 根据这个标识，后端控制接口是否可以访问，前端控制按钮是否显示
 后端实现： 在需要控制的接口上加上注解 @PreAuthorize("@ss.hasPermi('权限标识')")
            @ss是自定义的元注解，这里参考了若依的实现方式。 
            当然如果不想自己定义，可使用官方提供的方法hasAuthority、hasAnyAuthority、
            hasRole、hasAnyRole实现对资源或角色的灵活控制。           
 前端实现： 在html标签中加入 sec:authorize="@ss.hasPermi('权限标识')" 可以控制按钮是否能显示
 
 #个人见解
 实际项目中，虽然官方提供了（hasRole、hasAnyRole）方法，但个人不介意在代码中通过角色来控制
 资源，角色一但发生变化或者角色权限发生变化就不好处理，而且开发阶段根本无法确定每个角色的权限。
 所以我们通过控制每个接口就可以灵活操作权限，需要授权的接口分配给角色，角色再分配给用户实现灵活分配
 
 
 
 
 
 
    
