# 0 技术方案

`Springboot`+`Spring` + `SpringMVC` + `MyBatis` + `MyBatisPlus`：主流web框架；

`Shiro`：认证与鉴权（一大部分工作量在这里）；

`Redis`：缓存（例如：jedis/Lettuce....，本项目采用Lettuce作为Redis的后端实现）；

`RabbitMq`：消息中间件；

`ElasticSearch`：搜索模块；

`quartz`：定时器，定时发送文章（**此功能不一定实现**），定时清除OSS上的无效图片，将云存储上逻辑删除的资源间隔指定时间彻底清除，相当于完成回收站的类似功能（**此功能不一定实现**）；

`MySql`：数据库；

`Swagger`：Restful风格的web服务框架（方便与前端人员即时沟通；也可进行测试发送请求，实现postman类似的功能）；

`Spring`：启用缓存（提升IO性能）+  hibernate-validator 参数校验（基于JSR的参数校验规范，降低代码冗余度） + XSS过滤；

工具：Git（版本管理）+ 七牛云 （云存储）+  lombok（简化代码 + 日志记录） + Kaptcha（验证码工具）+ MyBatisPlus（通用Mapper + 代码生成器）+ MyBatisX（生成mapper的xml插件）+ jasypt （加密与解密） + 阿里云短信服务。

> 后续改进：使用SpringCloud进行微服务构建（或者使用Zookeeper + Dubbo 将项目变成一个RPC应用，方便进行水平伸缩拓展）。

# 1 创建数据库

## 1.1 数据库不使用外键

使用外键的优点：

- 降低开发成本，借助数据库产品自身的触发器可以实现表与关联表之间的数据一致性和更新。
- 可以做到开发人员和数据库设计人员的分工，可以为程序员承担更多的工作量。

使用外键的缺点：

- 互联网行业应用一般用户量大、并发度高，外键的存在很容易使数据库服务器成为性能瓶颈，易受IO限制。

  外键为何会带来性能问题？

  - 数据库维护管理外键带来的系统开销。
  - 外键等于把数据的一致性事务实现，全部交给数据库服务器完成；这同时会使数据的可操作性受限。
  - 当做一些涉及外键字段的增，删，更新操作之后，需要触发相关操作去检查，而不得不消耗系统资源。
  - 外键因需要请求对其他表内部加锁而容易出现死锁情况。

- 数据库服务器的水平扩展性差，而应用服务器的水平可伸缩性强，数据库外键的功能可以通过应用服务器的业务操作来实现。
- 数据库服务器的价格比应用服务器的价格高。

> 综上：一般互联网行业（并发量高）应用的数据库不推荐使用外键；本项目遵循不使用外键这一原则。

## 1.2 创建数据库

创建数据库`zcblog`，字符集采用`utf8`，校对规则为`utf8_unicode_ci`。

```mysql
CREATE DATABASE IF NOT EXISTS zcblog DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;
```

## 1.3 创建数据表

### 1.3.1 article表

`article`表主要用于存储博客文章相关内容：

```mysql
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '文章标题',
  `description` text COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文章描述',
  `author` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文章作者',
  `content` longtext COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文章内容',
  `content_format` longtext COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'html的content',
  `read_num` int(11) DEFAULT '0' COMMENT '阅读量',
  `like_num` int(11) DEFAULT '0' COMMENT '点赞量',
  `recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐文章：0-不推荐，1-推荐',
  `publish` tinyint(1) DEFAULT '0' COMMENT '是否发布：0-不发布，1-发布',
  `top` tinyint(1) DEFAULT '0' COMMENT '是否置顶：0-不置顶，1-置顶',
  `need_encrypt` tinyint(1) DEFAULT '0' COMMENT '是否加密：0-不加密，1-加密',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：更新时间',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='文章'
```

关于数据库中的数据类型：

- `TIMESTAMP`和`DATETIME`的区别：

  - **默认值不同**：`DATETIME`的默认值为null，`TIMESTAMP`的默认值为当前时间（`CURRENT_TIMESTAMP`）。
  - **存储空间不同**：`DATETIME`使用8字节的存储空间，`TIMESTAMP`的存储空间为4字节，`TIMESTAMP`比`DATETIME`的空间利用率更高。
  - **存储方式不同**：对于`TIMESTAMP`，它把客户端插入的时间从当前时区转化为UTC（世界标准时间）进行存储，查询时，将其又转化为客户端当前时区进行返回；对于DATETIME，不做任何改变，基本上是原样输入和输出。
  - **储存范围不同**：`TIMESTAMP`的存储范围为`1970-01-01 00:00:01.000000`到 `2038-01-19 03:14:07.999999`；`DATETIME`的存储范围为`1000-01-01 00:00:00.000000`到`9999-12-31 23:59:59.999999`

  > 总结：在mysql 5.6之前的版本，CURRENT_TIMESTAMP只能用于`TIMESTAMP`类型；5.6版本之后，CURRENT_TIMESTAMP也能用于`DATETIME`类型。考虑到兼容性问题和存储空间利用率问题，这里采用`TIMESTAMP`数据类型。

- `text`和`longtext`：均是用于存储字符串数据。
  - `tinyblob\blob\mediumblob\longblob`：最大分别可存储255个字节(2^8-1)、65535个字节(2^16-1)、(2^24-1)个字节、(2^32-1)个字节。
  - `tinytext\text\mediumtext\longtext`：最大分别可存储255个字节(2^8-1)、65535个字节(2^16-1)、(2^24-1)个字节、(2^32-1)个字节。
  - `text`和`blob`的本质区别：`text`用于存储非二进制字符串（字符字符串）、`blob`用于存储二进制字符串（字节字符串）。

- `tinyint(1)`和`tinyint(4)`的区别：
  - `tinyint(1)`和`tinyint(4)`中的1和4并不表示存储长度，二者均占用1个字节，取值范围为-128~127。
  - `tinyint(4)`：当字段指定了`zerofill`时，若实际值为2，则查询结果为0002，左边用0来填充。
  - `tinyint(1)`：由于MySQL中没有提供boolean数据类型，当tinyint(1)的值为0时表示false、非0表示true。从数据库中取出数据到Java时会自动转换为boolean类型。

### 1.3.2 encrypt表

`encrypt`表主要存储文章加密的密码和盐：

```mysql
CREATE TABLE `encrypt` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` int(11) NOT NULL COMMENT '加密文章的id',
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密密码',
  `salt` tinytext COLLATE utf8_unicode_ci COMMENT '加密盐',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：更新时间',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='文章加密'
```

### 1.3.3 gallery表

`gallery`表主要存储博客相册内容：

```mysql
CREATE TABLE `gallery` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `srl_url` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片源文件链接地址',
  `thumb_url` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片缩略图链接地址',
  `description` text COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：更新时间',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='相册'
```

### 1.3.4 tag表

`tag`表主要用于存储标签内容（**标签可以用来对相册内容和文章内容做标识**）：

```mysql
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标签名字',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：更新时间',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='标签'
```

### 1.3.5 tag_link表

之所以起名为`tag_link`表而不是`tag_article`表是为了方便后期进行拓展（**标签不仅可以用来对文章内容进行标识还可以对相册进行标识**）：

```mysql
CREATE TABLE `tag_link` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_id` int(11) DEFAULT NULL COMMENT '标签Id',
  `link_id` int(11) DEFAULT NULL COMMENT '关联Id',
  `type` int(4) DEFAULT NULL COMMENT '所属类别：0-文章，1-相册',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：更新时间',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='标签多对多维护表'
```

### 1.3.6 log_like表

`log_like`表主要记录游客点赞日志：

```mysql
CREATE TABLE `log_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '点赞类型',
  `params` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '自动填充：创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='点赞日志'
```

### 1.3.7 log_view表

`log_view`表主要记录游客浏览日志：

```mysql
CREATE TABLE `log_view` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '浏览类型',
  `method` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '自动填充：创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='阅读日志'
```

### 1.3.8 oss_resource表

`oss_resource`表主要用来统计云存储资源（记录写博客文章时产生的图片链接地址以及相册图片）。

> **注意事项**：对于相册图片而言，一般比较重要，因此需要实现逻辑删除功能；但是对于写博客文章时产生的图片，需要定时删除图片缓存，以免占用云储存空间。

```mysql
CREATE TABLE `oss_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `url` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源链接',
  `create_date` datetime DEFAULT NULL COMMENT '自动填充：创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：更新时间',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='云存储资源表'
```

### 1.3.9 sys_user表

`sys_user`表主要用来记录用户信息：

```mysql
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` tinytext COLLATE utf8_unicode_ci COMMENT '用户名',
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户密码',
  `email` tinytext COLLATE utf8_unicode_ci COMMENT '用户邮箱',
  `salt` tinytext COLLATE utf8_unicode_ci COMMENT '盐',
  `create_user_id` tinytext COLLATE utf8_unicode_ci COMMENT '创建者的user_id',
  `status` tinyint(1) DEFAULT NULL COMMENT '用户状态：0-禁用，1-正常',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT '用户'
```

### 1.3.10 sys_role表

`sys_role` 表主要用来记录用户角色：

```mysql
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色'
```

### 1.3.11 sys_user_role表

`sys_user_role`表主要作为`sys_user`和`sys_role`之间的中间表：

```mysql
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户与角色对应关系'
```

### 1.3.12 sys_menu表

`sys_menu`表主要作为系统菜单表、记录权限：

```mysql
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,菜单id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单id',
  `name` tinytext COLLATE utf8_unicode_ci COMMENT '菜单名称',
  `url` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路由地址',
  `perms` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限',
  `type` tinyint(4) DEFAULT NULL COMMENT '菜单类型：0-目录，1-菜单，2-按钮',
  `icon` tinytext COLLATE utf8_unicode_ci COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '同级菜单排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='菜单管理'
```

### 1.3.13 sys_role_menu表

`sys_role_menu`表主要用作`sys_role`表和`sys_menu`表之间的中间表：

```mysql
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色与菜单对应关系'
```

## 1.4 几点说明

### 1.4.1 自动填充/逻辑删除/乐观锁

自动填充/逻辑删除/乐观锁是MyBatisPlus提供的功能。

- 自动填充可以由插件自动生成值插入到数据库中，表中的`create_time`和`update_time`字段可以采用自动填充。
- 对于一些重要的数据，希望用户在操作的时候并不会真正的删除，采取一种逻辑删除的方式，既不影响业务逻辑功能，后期也可以针对一些危险操作进行数据恢复；弊端是可能会带来一些脏数据。
- 对于多线程操作，需要用到乐观锁，保证准确的增删改查；弊端是会带来性能损耗。

### 1.4.2 本项目中的表策略

根据数据的安全性，不同的表采用的策略不同。

- 同时需要添加`自动填充/逻辑删除/乐观锁`功能的数据表有`article`、`encrypt`、`gallery`、`tag`、`tag_link`、`oss_resource`。
- 需要添加`自动填充`功能的数据表有`log_like`、`log_view`、`sys_user`、`sys_role`。

### 1.4.3 关于权限管理

本项目中采用了5张表来完成权限的管理：`sys_user`、`sys_role`、`sys_user_role`、`sys_menu`、`sys_role_menu`。其中`sys_menu`表中包含了权限相关的内容。

主流的权限管理表应该至少需要10张表，包括`权限表`、`角色表`、`组表`、`用户表`4张主表；`用户权限表`、`用户角色表`、`用户组表`、`角色权限表`、`组角色表`、`组权限表`6张中间表。本项目相当于做了一定的简化。关于"用户·角色·权限·表"的设计可以参见相关博客文章：**[用户·角色·权限·表的设计](https://blog.csdn.net/weixin_42476601/article/details/82346740)**









































