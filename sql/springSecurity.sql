/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.7.38 : Database - spring_security
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`spring_security` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `spring_security`;

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` varchar(30) NOT NULL COMMENT '主键',
  `menu_name` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `parent_id` varchar(30) DEFAULT NULL COMMENT '父id',
  `order_num` int(10) DEFAULT NULL COMMENT '排序',
  `path` varchar(200) DEFAULT NULL COMMENT '路径',
  `perms` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `menu_type` varchar(2) DEFAULT NULL COMMENT '类型，M目录 C菜单 F按钮',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `menu` */

insert  into `menu`(`id`,`menu_name`,`parent_id`,`order_num`,`path`,`perms`,`menu_type`,`create_time`,`update_time`,`remark`,`icon`) values ('1','系统管理','0',1,NULL,NULL,'M',NULL,NULL,'系统管理目录','el-icon-setting'),('2','用户管理','1',1,'/user/indexView','system:user:list','C',NULL,NULL,NULL,'el-icon-user-solid'),('3','菜单管理','1',2,NULL,'system:menu:list','C',NULL,NULL,NULL,'el-icon-picture');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` varchar(30) NOT NULL COMMENT '主键',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '权限标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `role` */

insert  into `role`(`id`,`role_name`,`role_key`) values ('1','超级管理员','ROLE_admin'),('2','普通管理员','ROLE_common');

/*Table structure for table `role_menu` */

DROP TABLE IF EXISTS `role_menu`;

CREATE TABLE `role_menu` (
  `id` varchar(30) NOT NULL COMMENT '主键',
  `role_id` varchar(30) DEFAULT NULL COMMENT '权限id ',
  `menu_id` varchar(30) DEFAULT NULL COMMENT '菜单id ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_menu` */

insert  into `role_menu`(`id`,`role_id`,`menu_id`) values ('1','2','2'),('2','2','3'),('3','2','1');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(30) NOT NULL COMMENT '主键',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `phone` varchar(30) DEFAULT NULL COMMENT '手机号',
  `sex` char(1) DEFAULT NULL COMMENT '性别 1男 2女',
  `address` varchar(300) DEFAULT NULL COMMENT '地址',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '昵称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`phone`,`sex`,`address`,`nick_name`) values ('0','admin','$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq','18888888888','1','似懂非懂','管理员'),('1','test','$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq','15356789078','2','贵州省贵阳市云岩区','管理员'),('6','root','$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq','15356789078','1','贵州省贵阳市云岩区','小灰灰');

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `id` varchar(30) NOT NULL COMMENT 'id',
  `user_id` varchar(30) DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(30) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `user_role` */

insert  into `user_role`(`id`,`user_id`,`role_id`) values ('1','1','1'),('2','1','2'),('3','1','3'),('4','1','4'),('7','6','2');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
