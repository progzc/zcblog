# 0 技术方案

`Springboot`+`Spring` + `SpringMVC` + `MyBatis` + `MyBatisPlus`：主流web框架；

`Shiro`：认证与鉴权（一大部分工作量在这里）；

`Redis`：缓存（例如：jedis/Lettuce....，本项目采用Lettuce作为Redis的后端实现）；

`RabbitMq`：消息中间件；

`ElasticSearch`：搜索模块；

`quartz`：定时任务框架，定时发送文章（**此功能不一定实现**），定时清除OSS上的无效图片，将云存储上逻辑删除的资源间隔指定时间彻底清除，相当于完成回收站的类似功能（**此功能不一定实现**）；

`MySql` + `Druid`：数据库 + 连接池；

`Swagger`：RESTful风格的API框架（方便与前端人员即时沟通；也可进行测试发送请求，实现postman类似的功能）；

`Spring`：启用缓存（提升IO性能）+  hibernate-validator 参数校验（基于JSR的参数校验规范，降低代码冗余度） + XSS过滤；

工具：Git（版本管理）+ 七牛云 （云存储）+  lombok（简化代码 + 日志记录） + Kaptcha（验证码工具）+ MyBatisPlus（通用Mapper + 代码生成器）+ MyBatisX（生成mapper的xml插件）+ Jasypt （加密与解密） + 阿里云短信服务。

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
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
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
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
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
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动填充：更新时间',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLAeTE=utf8_unicode_ci COMMENT='云存储资源表'
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
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户创建时间',
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

> 主流的权限管理表应该至少需要10张表，包括`权限表`、`角色表`、`组表`、`用户表`4张主表；`用户权限表`、`用户角色表`、`用户组表`、`角色权限表`、`组角色表`、`组权限表`6张中间表。本项目相当于做了一定的简化。关于"用户·角色·权限·表"的设计可以参见相关博客文章：**[用户·角色·权限·表的设计](https://blog.csdn.net/weixin_42476601/article/details/82346740)**

# 2 项目搭建

## 2.1 项目模块结构(基于功能)

使用mavon搭建工程，项目目录如下：

```yaml
zcblog-backend         # 父模块
|————zcblog-core       # 核心基础类：yml配置、Entity、工具类、xss过滤等
|		|————pom.xml   # 引入依赖
|		|————src
|————zcblog-authorize  # 登录与鉴权：Shiro
|		|————pom.xml   # 依赖zcblog-core
|		|————src
|————zcblog-manage     # 博客后台管理系统的服务请求
|		|————pom.xml   # 依赖zcblog-authorize
|		|————src
|————zcblog-client     # 博客前台系统的服务请求
|		|————pom.xml   # 依赖zcblog-manage
|		|————src
|————zcblog-search     # 搜索模块 + 消息中间件：Elasticsearch、RabbitMq
|		|————pom.xml   # 依赖zcblog-client
|		|————src       # 项目启动入口：com.progzc.blog.BlogRunApplication
|————pom.xml           # 引入SpringBoot启动依赖；管理jar包，统一使所有子模块依赖项的版本。
```

几点说明：

- 整个项目使用Mavon进行构建，利用Git进行版本管理。

- zcblog-backend为父模块，父模块有三个作用：引入SpringBoot启动依赖；管理jar包，统一所有子模块依赖项的版本、统一编译所有子模块。

- 父模块下按照项目的功能划分有5个子模块，子模块之间的依赖关系为：

  > `zcblog-core`-->`zcblog-authorize`-->`zcblog-manage`-->`zcblog-client`-->`zcblog-search`

- 启动类放置在`zcblog-search`模块中，入口文件为`com.progzc.blog.BlogRunApplication`。

## 2.2 项目模块结构新构思(基于服务)

在设计之初，考虑到后续进行水平扩展的另一种模块结构的设计（**即时将灵感记录下来，预计本项目完成之后，再对项目进行改进**）。如下所示：

```yaml
zcblog-backend         # 父模块
|————zcblog-core       # 核心基础类：Entity、工具类、xss过滤等
|		|————pom.xml   # 引入依赖
|		|————src
|————zcblog-interface  # 存放服务接口
|		|————pom.xml   # 依赖zcblog-core
|		|————src
|————zcblog-authorize  # 登录与鉴权：Shiro
|		|————pom.xml   # 依赖zcblog-interface
|		|————src
|————zcblog-consumer   # 服务消费者：controller
|		|————pom.xml   # 依赖zcblog-authorize，单独部署应用
|		|————src       # 应用启动入口：com.progzc.blog.ConsumerRunApplication
|————zcblog-provider   # 服务消费者：serviceImpl、do层、*Mapper.xml
|		|————pom.xml   # 依赖zcblog-interface，单独部署应用
|		|————src       # 应用启动入口：com.progzc.blog.ProviderRunApplication
|————zcblog-timer      # 定时器，进行任务调度
|		|————pom.xml   # 依赖zcblog-interface，单独部署应用
|		|————src	   # 应用启动入口：com.progzc.blog.TimerRunApplication
|————pom.xml           # 管理jar包，统一使所有子模块依赖项的版本。
```

> **特别说明**：这种模块结构的好处是可以进行水平扩展，实现分布式部署。但是考虑到本博客项目最终实际是运行到自己购买的云服务器上，而本人所购买的服务器配置较低，若在单机上模拟分布式应用，内存比较紧张。这里先将这种模块结构构思出来，待本项目完成之后，重新新建一个项目对本项目进行改进，完成项目的分布式部署。

## 2.3 项目Jar包管理

在父模块`zcblog-backend`的`pom.xml`进行jar包管理：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!--项目相关信息-->
    <groupId>com.progzc</groupId>
    <artifactId>blog</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <name>zcblog-backend</name>
    <description>Clouds' Blog</description>

    <!--项目子模块-->
    <modules>
        <module>zcblog-core</module>
        <module>zcblog-authorize</module>
        <module>zcblog-manage</module>
        <module>zcblog-client</module>
        <module>zcblog-search</module>
    </modules>

    <!--引入SpringBoot启动依赖-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/><!-- lookup parent from repository -->
    </parent>

    <!--定义所需各种框架的版本-->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding><!--项目采用utf-8字符集编码-->
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven.compiler.encoding>UTF-8</maven.compiler.encoding><!--编译时的编码-->
        <java.version>1.8</java.version><!--使用java1.8版本-->
        <mybaits.version>1.3.2</mybaits.version><!--DO层使用MyBatis操作数据库-->
        <mybatisplus.version>3.0.1</mybatisplus.version><!--使用MyBatisPlus增强MyBatis的功能-->
        <druid.version>1.1.10</druid.version><!--数据库连接池使用Druid-->
        <shiro.version>1.4.0</shiro.version><!--使用Shiro进行登录与鉴权-->
        <swagger.version>2.9.2</swagger.version><!--使用Swagger UI生成接口文档-->
        <kaptcha.version>0.0.9</kaptcha.version><!--来自谷歌的验证码工具kaptcha-->
        <redis.pool.version>2.6.0</redis.pool.version><!--使用redis实现缓存-->
        <commons.lang.version>2.6</commons.lang.version><!--常用的工具包-->
        <commons.fileupload.version>[1.3.3,)</commons.fileupload.version><!--实现文件上传-->
        <commons.io.version>2.5</commons.io.version><!--IO工具包-->
        <jasypt.version>2.1.0</jasypt.version><!--加密与解密-->
        <qiniu.version>[7.2.0, 7.2.99]</qiniu.version><!--使用七牛云的OSS作为云储存-->
        <springboot.version>2.1.2.RELEASE</springboot.version><!--Spring的版本-->
        <lombok.version>1.18.4</lombok.version><!--简化代码-->
        <mysql.version>8.0.11</mysql.version><!--数据库使用MySQL-->
    </properties>

    <!--管理jar包，统一使所有子模块依赖项的版本-->
    <dependencyManagement>
        <dependencies>
            <!--lombok-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
                <version>${lombok.version}</version>
            </dependency>
            <!--Swagger-->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>${swagger.version}</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>${swagger.version}</version>
            </dependency>
            <!--常用工具包-->
            <dependency>
                <groupId>commons-lang</groupId>
                <artifactId>commons-lang</artifactId>
                <version>${commons.lang.version}</version>
            </dependency>
            <!--文件上传-->
            <dependency>
                <groupId>commons-fileupload</groupId>
                <artifactId>commons-fileupload</artifactId>
                <version>${commons.fileupload.version}</version>
            </dependency>
            <!--IO工具包-->
            <dependency>
                <groupId>commons-io</groupId>
                <artifactId>commons-io</artifactId>
                <version>${commons.io.version}</version>
            </dependency>
            <!--jasypt-->
            <dependency>
                <groupId>com.github.ulisesbocchio</groupId>
                <artifactId>jasypt-spring-boot-starter</artifactId>
                <version>${jasypt.version}</version>
            </dependency>
            <!--Spring aop-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-aop</artifactId>
                <version>${springboot.version}</version>
            </dependency>
            <!--Spring Web-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <version>${springboot.version}</version>
            </dependency>
            <!--Shiro-->
            <dependency>
                <groupId>org.apache.shiro</groupId>
                <artifactId>shiro-spring</artifactId>
                <version>${shiro.version}</version>
            </dependency>
            <!--Kaptcha-->
            <dependency>
                <groupId>com.github.axet</groupId>
                <artifactId>kaptcha</artifactId>
                <version>${kaptcha.version}</version>
            </dependency>
            <!--七牛云-->
            <dependency>
                <groupId>com.qiniu</groupId>
                <artifactId>qiniu-java-sdk</artifactId>
                <version>${qiniu.version}</version>
            </dependency>
            <!--单元测试-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <version>${springboot.version}</version>
            </dependency>
            <!--MyBatis-->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybaits.version}</version>
            </dependency>
            <!--MySQL-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!--MyBatisPlus-->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatisplus.version}</version>
            </dependency>
            <!--Druid-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>${druid.version}</version>
            </dependency>
            <!--Spring缓存-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-cache</artifactId>
                <version>${springboot.version}</version>
            </dependency>
            <!--redis-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-redis</artifactId>
                <version>${springboot.version}</version>
            </dependency>
            <!--连接池-->
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-pool2</artifactId>
                <version>${redis.pool.version}</version>
            </dependency>
            <!--Elasticsearch-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
                <version>${springboot.version}</version>
            </dependency>
            <!--Rabbitmq-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-amqp</artifactId>
                <version>${springboot.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <!--添加mavon插件-->
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>

</project>
```

## 2.4 项目配置

在`zcblog-core`模块中使用yml进行项目配置，根据开发环境的不同，分为开发环境配置文件（`application-dev.yml`）、测试环境配置文件（`application-test.yml`）和生产环境配置文件（`application-prod.yml`）。将`application-dev.yml`、`application-test.yml`和`application-prod.yml`中的相同部分抽离到`application.yml`中。

### 2.4.1 application.yml

在`application.yml`配置文件中主要进行web应用服务器（本项目使用`Tomcat`）的配置、Spring web的配置、MyBatis + MyBatisPlus的配置。

```yaml
# 配置web应用服务器
server:
  tomcat:
    uri-encoding: utf-8 # 网络请求字符集为utf-8
    max-threads: 1000 # 最多支持1000个线程
    min-spare-threads: 30 # 至少有30个空闲线程
  # 端口分配：博客前台端口8080；博客管理前台端口8081；博客后台端口8082
  port: 8082
  connection-timeout: 5000ms # 请求超时时间5s
  servlet:
    #项目映射路径，相当于网络请求为 http://localhost:8082/blog
    context-path: /blog

# Spring配置
spring:
  profiles:
    # 配置环境：开发dev,测试test,生产prod
    active: dev
  jackson:
    serialization:
      # 从数据库返回的时间戳是long型的毫秒值
      write-dates-as-timestamps: true
  servlet:
    multipart:
      max-file-size: 100MB # 单个文件上传最大100MB
      max-request-size: 100MB # 总上传文件最大100MB
  mvc:
    # restFul风格：当找不到页面时，正常抛出错误，不跳转页面
    throw-exception-if-no-handler-found: true
    # 映射static资源文件(如js/css/img...)
    static-path-pattern: /static/**
  resources:
    # 不要为资源文件建立默认映射
    add-mappings: false
  rabbitmq:
    listener:
      direct:
        acknowledge-mode: manual # 表示该监听器手动应答消息

# MyBatisPlus配置
mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml # 指定mapper文件位置
  type-aliases-package: com.progzc.blog.entity.* # 配置别名，多个package使用逗号分隔
  global-config:
    # 配置数据库
    db-config:
      id-type: auto # 主键策略：数据库自增
      field-strategy: not_empty # 字段策略：非空判断
      logic-delete-value: 1 # 1表示逻辑删除（默认值是1）
      logic-not-delete-value: 0 # 0表示未删除（默认值是0）
  # MyBatis原生配置
  configuration:
    map-underscore-to-camel-case: true # 驼峰下划线自动转换
    cache-enabled: false # 不开启二级缓存
```

下面对几个重要的配置做解释：

- **id-type**：配置主键生成策略，取值如下。
  - `auto`：数据库主键自增（**本项目这种策略**）。
  - `none`：根据雪花算法生成主键。
  - `input`：insert前自行set主键值（可以通过自己注册填充插件进行填充）。
  - `assign_id`：分配id；使用接口IdentifierGenerator的方法nextId（默认实现类为DefaultIdentifierGenerator雪花算法）。
  - `assign_uuid`：分配UUID；使用接口IdentifierGenerator的方法nextUUID（默认default方法）。

- **field-strategy**：配置字段策略，取值如下。

  - ignored：查询时会将未设置字段以null自动填充。

    ```java
    // 若设置ignored，会忽略掉null值得判断。可以为更新对象设空值！
    public void updateUserTest(){
        User user = new User();
        user.setId(1);
        user.setState((byte) 1);
        user.setAddress(null);
        userService.updateById(user);
    }
    // 输出结果
    ==> Preparing: UPDATE user SET address=?, state=? WHERE id=? 
    ==> Parameters: null, 1(Byte), 1(Integer)
    ```

  - not_null：会进行null检查；通过接口更新数据时字段为null值时将不更新进数据库。

  - not_empty：会对字段值进行null和''比较检查；通过接口更新数据时字段为null值和''值时将不更新进数据库（**本项目采用这种策略**）。

### 2.4.2 application-dev.yml

`application-dev.yml`中主要进行**开发阶段**的数据库和Druid连接池、Redis、Elasticsearch、RabbitMQ、MyBatisPlus（特定配置）、七牛云、jasypt。

```yaml
spring:
  # 配置数据库
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  # 使用Druid连接池
    driver-class-name: com.mysql.cj.jdbc.Driver  # 使用MySQL数据库
    druid:
      # 配置数据库连接地址并进行相关设置（允许进行批处理、使用utf-8字符集、不进行SSL连接、使用格林威治时间）
      url: jdbc:mysql://localhost:3306/zcblog?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT
      username: root  # 数据库用户名
      password: root  # 数据库密码
      initial-size: 10  # 初始连接数10个
      max-active: 100  # 最大连接数100个
      min-idle: 10  # 最小空闲数是10个
      max-wait: 60000  # 最长等待时间为60s
      # 开启缓存preparedStatement(PSCache)，PSCache对支持游标的数据库性能提升巨大，比如说oracle;在MySQL下建议关闭。
      pool-prepared-statements: true
      # 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。
      # 在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
      max-pool-prepared-statement-per-connection-size: 20
      time-between-eviction-runs-millis: 60000  # Destroy线程检测连接的间隔时间为60s
      min-evictable-idle-time-millis: 300000  # 一个连接在池中最小生存的时间为300s
      test-while-idle: true  # 若空闲时间大于timeBetweenEvictionRunsMillis,则执行validationQuery检测连接是否有效;不影响性能,但可保证安全性.
      test-on-borrow: false  # 申请连接时不执行validationQuery检测连接是否有效,会提升性能
      test-on-return: false  # 归还连接时不执行validationQuery检测连接是否有效,会提升性能
      stat-view-servlet:
        enabled: true  #  启用监控页面的配置
        url-pattern: /druid/*  # 设置监控页面的url
        login-username: admin  # 设置监控页面的登录名
        login-password: admin  # 设置监控页面的登录密码
      filter:
        # 监控统计
        stat:
          db-type: mysql  # 数据库为MySQL
          log-slow-sql: true  # 记录慢SQL
          slow-sql-millis: 1000  # 定义执行时间1s以上的为慢SQL
          merge-sql: false  # 禁止合并SQL
        wall:
          db-type: mysql  # 数据库为MySQL
          enabled: true  # 防止SQL注入
          config:
            multi-statement-allow: true  # 允许进行批处理(spring.datasource.druid.url也要进行配置)
  # 配置redis
  redis:
    host: localhost  # 配置主机为本机
    port: 6379  # 配置端口号
    timeout: 6s  # 配置连接池超时时长为6s
    lettuce:
      pool:
        max-active: 1000  # 连接池最大连接数
        max-wait: -1ms  # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-idle: 10  # 连接池中的最大空闲连接数
        min-idle: 5  # 连接池中的最小空闲连接数
  # 配置Elasticsearch
  data:
    elasticsearch:
      cluster-name: zcblog-cluster  # 配置集群名称
      cluster-nodes: 127.0.0.1:9301  # 配置集群中某一节点的地址
  # 配置RabbitMQ
  rabbitmq:
    host: 192.168.175.135  # 配置主机（本项目RabbitMQ运行在虚拟机上）
    port: 5672  # 配置端口号
    username: guest  # 配置用户名
    password: guest  # 配置密码

# 配置MyBatisPlus
mybatis-plus:
  global-config:
    refresh: true  # 刷新Mapper,只在开发环境打开

# 配置七牛云（变量）
oss:
  qiniu:
    domain: http://qhnmn5y5g.hn-bkt.clouddn.com  # 七牛云外链域名
    prefix: blog # 前缀,相当于项目路径
    accessKey: ENC(加密数据源)  # 配置AccessKey（非明文）
    secretKey: ENC(加密数据源)  # 配置SecretKey（非明文）
    bucketName: progzc-blog # 配置空间名

# 配置加密和解密
jasypt:
  encryptor:
    password: 密码盐  # 配置密码盐
```

### 2.4.3 application-test.yml

`application-test.yml`配置文件中的内容与`application-dev.yml`基本一致，只需要根据实际需要稍加修改即可。

```yaml
mybatis-plus.global-config.refresh: false  # 刷新Mapper,只在开发环境打开
```

### 2.4.4 application-prod.yml

`application-prod.yml`配置文件中的内容与`application-dev.yml`基本一致，只需要根据实际需要稍加修改即可。

```yaml
mybatis-plus.global-config.refresh: false  # 刷新Mapper,只在开发环境打开
```

### 2.4.5 jasypt明文加密

为了安全，项目配置文件里的密码（以及有些重要的用户名、密钥）一般不能采用明文显示，需要进行加密处理。这里采用`jasypt`进行加密。

使用步骤：

1. 引入工具依赖包（在`zcblog-core`模块中引入）：

   ```xml
   <!--jasypt加密解密-->
   <dependency>
   	<groupId>com.github.ulisesbocchio</groupId>
   	<artifactId>jasypt-spring-boot-starter</artifactId>
   </dependency>
   ```

2. 在控制台下，直接使用jasypt工具包对数据源进行加密。采用如下命令：

   ```java
   java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=数据源 password=密码盐 algorithm=加密方式
   ```
数据源为yml配置文件中中需要加密的密码（或用户名、密钥等），默认的加密方式为：PBEWithMD5AndDES，密码盐为yml配置文件中`jasypt.encryptor.password`的值。

3. 将第2步生成输出的`加密数据源`填充到yml配置文件中的原明文位置，形式如下：

   ```java
   ENC(加密数据源)
   // 举例
   oss.qiniu.accessKey: ENC(hF4gC+N5O30Z5WEtKxOD1mSZsRox0MBg3YAjBtfIMvetCLWQfBKSTz3HpdMqiaTpHVxriYk0aEo=)
   ```

4. 数据源的使用：

   ```java
   // 4.1 在yml文件中配置密码盐
   jasypt.encryptor.password: 密码盐
   
   // 4.2 在yml文件中将明文用加密数据源替换
   oss.qiniu.accessKey: ENC(加密数据源)
   
   // 4.3 在Springboot中自动使用jasypt解密获取明文
   public class JasyptTest {
       @Autowired
       StringEncryptor stringEncryptor; // 密码解码器自动注入
       
       @Value("${oss.qiniu.accessKey}")
       private String accessKey; // jasypt会自动对accessKey进行解密
    
       @Test
       public void test() {
           System.out.println("密码:" + accessKey);
       }
   }
   ```

> **注意事项：**由于**加密方式**默认为`PBEWithMD5AndDES`，yml配置文件中又会暴露**密码盐**，非法分子很容易就可以拿到**加密方式**和**密码盐**对**加密数据源**进行解密得到**数据源**，这是很不安全的一种操作。
>
> **实际工作中：**会自定义一个**加密解密类**（实现`StringEncryptor`接口），然后将其注入到使用的项目中（@Autowired），在该类中可以对**加密数据源**进行解密操作得到**数据源**；**加密解密类**会存放在服务器上特定的位置（类似于RPC应用），这样就保证了密码的安全性。

> 参考博客文章：**[SpringBoot配置文件属性内容加解密](https://blog.csdn.net/cts529269539/article/details/79024436)**

### 2.4.6 log日志配置

#### 2.4.6.1 日志系统的介绍

常用的日志系统抽象层有`Slf4j`，实现层有`Log4j`、`Logback`...，SpringBoot默认的日志系统是`Slf4j(抽象层) + LogBack(实现层)`，默认的日志级别是`info`。

> **日志级别**：`trace-->debug-->info-->warn-->error-->fatal-->off（由低到高）`，SpringBoot默认日志级别为`info`，控制台默认只会打印`info`及以上的日志。

如果需要在程序中主动进行日志记录（如`log.debug("debug...")`、`log.info("info...")`、`log.error("error...")`......），可以借助与`lombok`插件简化主动进行日志记录的流程。

```yaml
# 简化主动进行日志记录的流程
# 1.安装lombok插件
# 2.在类上添加@Slf4j注解
# 3.在程序代码块中主动进行日志记录，如：log.debug("debug...");
#  3.1 程序代码块中可以使用{}占位符来拼接字符串，如：log.info("name:{} , age:{}", name, age);
```

#### 2.4.6.2 常用的日志配置项

常用的日志配置如下：

```yaml
# 日志基础配置
logging.level.root: 级别名  # 定义项目日志级别（默认是info）
logging.level.包名: 级别名  # 设置指定包的日志级别（未指定的包按照root的日志级别执行）
logging.file: springboot.log  # 配置日志输出到springboot.log文件（若未配置logging.path，则日志输出到当前项目根路径下）
logging.path: /springboot/log  # 配置日志输出到当前项目所在磁盘根路径下的/springboot/log目录下（也可以使用绝对路径）
logging.pattern.console: 日志输出格式  # 配置控制台输出的日志格式
logging.pattern.file: 日志输出格式  # 配置日志文件的日志格式

# 日志进阶配置（由于不常用，详见博客文章）
logging.config: 日志配置文件  # 可以导入.xml日志配置文件
# 日志文件按照指定格式、大小(默认按照10M)进行分割
# 控制日志的颗粒度（不同类拥有不同的日志输出格式和日志级别）
# 控制是否向上级传递日志信息（默认是true）

# 日志输出格式说明
# %d 输出日期时间，
# %thread 输出当前线程名，
# %-5level 输出日志级别，左对齐5个字符宽度
# %logger{50} 输出全类名最长50个字符，超过按照句点分割
# %msg 日志信息
# %p 输出日志信息的优先级
# %m 输出代码中指定的具体日志信息
# %n 换行符
# 实例：logging.pattern.console: %d{yyyy-MM-dd} === [%thread] === %-5level === %logger{50} === - %msg%n
# 实例：logging.pattern.file: %d{yyyy-MM-dd} === [%thread] === %-5level === %logger{50} === - %msg%n

# 一般希望日志文件的名称和日志目录不要写死，可以采用如下操作
logging.path: 日志目录  # 配置日志文件的目录
logging.file.name: ${logging.path}/${spring.application.name}.log  # 动态生成日志文件
```
#### 2.4.6.3 本项目的日志配置

本项目的日志配置在yml中进行配置。`开发及测试环境下`的日志配置如下：

```yaml
# 配置日志
logging:
  level:
    root: info # 开发测试环境根级别配置info级别日志
    com.progzc.blog: debug  # 开发测试环境项目配置debug级别日志
    com.prog.blog.mapper: trace  # 开发测试环境dao层配置trace级别日志
```

`生产环境下`的日志配置如下：

```yaml
# 配置日志
logging:
  level:
    root: error  # 生产环境根级别配置error级别日志
    com.progzc.blog: error  # 生产环境项目配置error级别日志
    com.progzc.blog.mapper: error  # 生产环境dao层配置error级别日志
  file: error.log  # 生产环境将错误日志输出到当前项目根路径下的error.log文件
```

> 参考博客文章：**[SpringBoot日志配置](https://blog.csdn.net/qq_44316726/article/details/108979727)**、[日志格式化符号解释](https://www.cnblogs.com/dong-dong-dong/p/9547136.html)、**[日志配置进阶与lombok简化主动生成日志](https://blog.csdn.net/Inke88/article/details/75007649)**、

## 2.5 关于RESTful API

### 2.5.1 RESTful API简介

**REST（Representational State Transfer）**：即表现层状态转化。它是一种互联网应用程序的**API设计理念**：可以用URL定位资源，用HTTP动词（GET、POST、DELETE、PUT）描述操作来解释什么是REST。GET用来获取资源，POST用来新建资源（也可以用于更新资源），PUT用来更新资源，DELETE用来删除资源。

**RESTful API**：基于REST构建的API就是Restful风格。优点是**可以通过一套统一的API接口为 Web，iOS和Android提供服务**，特别适合于前后端分离的系统。

### 2.5.2 RESTful API的设计细节

#### 2.5.2.1 URL设计

1. 客户端发出的数据操作指令都是**"动词 + 宾语"**的结构，如`GET /articles`。动词通常就是5种HTTP方法，对应CRUD操作。

```yaml
GET: 读取（Read）
POST: 新建（Create）
PUT: 更新（Update）
PATCH: 更新（Update），通常是部分更新
DELETE: 删除（Delete）

# 有些客户端只能使用GET和POST这两种方法。服务器必须接受POST模拟其他三个方法（PUT、PATCH、DELETE），解决方法是客户端发出的 HTTP 请求，要加上X-HTTP-Method-Override属性，告诉服务器应该使用哪一个动词，覆盖POST方法。
POST /api/Person/4 HTTP/1.1  
X-HTTP-Method-Override: PUT
```

2. 宾语（即API的URL），是HTTP动词作用的对象。**宾语应该是名词，不能是动词**。

```yaml
/articles  # URL正确
/getAllCars  # URL错误
/createNewCar  # URL错误
/deleteAllRedCars  # URL错误
```

3. 宾语（即API的URL）使用单数还是复数一般取决于实际使用场景。这里我们**建议统一使用复数URL**。

```yaml
GET /articles  # 读取所有文章，URL使用复数 
GET /articles/2  # 读取id为文章，URL使用复数（与下面的方式相比各有道理，这里建议统一使用复数URL）
GET /article/2  # 读取id为文章，URL使用单数 
```

4. **避免多级URL**。这是因为多级URL不利于扩展，语义不明确；更好的做法是：**除了第一级，其他级别都用查询字符串表达**。

```yaml
GET /authors/12/categories/2  # 获取某个作者的某一类文章；URL采用多级URL，不正确
GET /authors/12?categories=2  # 获取某个作者的某一类文章；URL采用一级URL+查询字符串表达，正确

GET /articles/published  # 查询已发布的文章；URL采用多级URL，不正确
GET /articles?published=true  # 查询已发布的文章；URL采用一级URL+查询字符串表达，正确
```

#### 2.5.2.2 状态码

**状态码必须精确**。针对客户端的每一次请求，服务器都必须给出响应。**响应包括 HTTP 状态码和数据两部分**。

```yaml
# 状态码分类
1xx: 相关信息  # API不需要1xx状态码
2xx: 操作成功
  GET: 200 OK  # 表示GET请求操作成功
  GET: 202 Accepted  # 表示服务器已经收到请求，但还未进行处理，会在未来再处理，通常用于异步操作
  POST: 201 Created  # 表示生成了新的资源
  PUT: 200 OK  # 表示更新了资源
  PATCH: 200 OK  # 表示更新了部分资源
  DELETE: 204 No Content  # 表示资源已经不存在
3xx: 重定向  # API不需要301(永久重定向)、302(暂时重定向)状态码
  # 303 See Other: 最常用，表示参考另外一个URL(浏览器不会自动跳转，让用户来抉择)
4xx: 客户端错误
  # 400 Bad Request: 服务器不理解客户端的请求，未做任何处理
  # 401 Unauthorized: 用户未提供身份验证凭据，或者没有通过身份验证
  # 403 Forbidden: 用户通过了身份验证，但是不具有访问资源所需的权限
  # 404 Not Found: 所请求的资源不存在，或不可用
  # 405 Method Not Allowed: 用户已经通过身份验证，但是所用的HTTP方法不在他的权限之内
  # 410 Gone: 所请求的资源已从这个地址转移，不再可用
  # 415 Unsupported Media Type: 客户端要求的返回格式不支持；比如：API只能返回JSON格式，但是客户端要求返回XML格式
  # 422 Unprocessable Entity: 客户端上传的附件无法处理，导致请求失败
  # 429 Too Many Requests: 客户端的请求次数超过限额
5xx: 服务器错误  # 为了安全，API不会向用户透露服务器的详细信息，只要500和503两个状态码即可
  # 500 Internal Server Error: 客户端请求有效，服务器处理时发生了意外
  # 503 Service Unavailable: 服务器无法处理请求，一般用于网站维护状态
```

#### 2.5.2.3 服务器响应

1. **不要返回纯文本**。API返回的数据格式，不能是纯文本，而应该是一个JSON对象。
   - 服务器响应的HTTP 头设置为`Content-Type: application/json`。
   - 客户端请求时，需要告知服务器，发送的是JSON格式，请求的 HTTP 头的设置为`ACCEPT: application/json`

2. 发生错误时，不要返回200状态码，要严格按照状态码的准确含义来体现。

3. **提供API连接**。让API的使用者知道，URL是如何设计的。这样，用户只需记住一个URL，就可以发现其他URL。

   ```yaml
   # 例：GitHub的API都在https://api.github.com/这个域名。访问它，就可以得到其他URL。
   ```

### 2.5.3 本项目的API设计

本项目的API设计会尽可能遵循RESTful原则。**具体体现形式**：

1. URL采用"动词+宾语"结构。可以在**SrpingMVC控制层**通过@GetMapping、@PostMapping、@PutMapping、@DeleteMapping等注解来实现。
2. URL宾语统一使用复数名词。
3. URL会尽可能避免产生多级，会尽量采用第一级+查询字符串来表达。
4. 将服务器响应封装到`Result.java`中，`Result.java`中的状态码和数据会遵循RESTful原则。
5. 服务端提供`JsonUtils.java`工具类，负责对象与JSON字符串之间的转换。
6. 本项目使用`Swagger`来实现前后端人员API设计的及时沟通，API地址：`http://主机地址:端口号/blog/v2/api-docs`。

> 参考博客文章：**[阮一峰-Restful API最佳实践](http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html)**、[Rest服务和Restful API](https://blog.csdn.net/shangrila_kun/article/details/89026968)、[Restful风格的API接口开发教程](https://www.imooc.com/article/28250)

# 3 代码生成器

## 3.1 自定义代码生成工具类

借助于MybatisPlus，可以根据数据库中的表快速帮我们生成entity、controller、service、serviceImpl、mapper、xml。这样带来了两个好处：一是节省了大量的时间；二是保证了准确性（不会出现成员变量与表中字段名对应不上的情况）。

在`CodeGeneratorUtils.java`中编写自动生成代码的逻辑，为了便于理解，代码中采用了大量的注释。

```java
public class CodeGeneratorUtils {

    public static String projectPath = System.getProperty("user.dir");
    /**
     * 根据表名自动生成代码
     * @param tableName
     * @param moduleName
     * @param category
     */
    public static void codeGenerator(String tableName, String moduleName, String category){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "\\zcblog-backend\\zcblog-core\\src\\main\\java");
        gc.setAuthor("zhaochao");
        gc.setOpen(false); // 不打开输出目录
        gc.setBaseResultMap(true); // 开启BaseResultMap
        gc.setBaseColumnList(true); // 开启baseColumnList
        gc.setSwagger2(true); // 实体属性添加Swagger2注解
        gc.setControllerName("%sController"); // controller命名方式为在末尾添加Controller
        gc.setServiceName("%sService"); // service命名方式为在末尾增加Service
        gc.setServiceImplName("%sServiceImpl"); // service实现类命名方式为在末尾添加ServiceImpl
        gc.setMapperName("%sMapper"); // mapper命名方式为在末尾增加Mapper
        gc.setIdType(IdType.AUTO); // 默认主键自增类型为数据库自增
        gc.setDateType(DateType.ONLY_DATE); // 设置日期格式
        gc.setFileOverride(false); // 不覆盖原来文件（否则会比较危险），也可在cfg.setFileCreate中进行自定义配置
        mpg.setGlobalConfig(gc); // 为代码生成器注入全局配置

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/zcblog?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc); // 添加数据源配置

        // 包配置（后面手动单独配置）
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.progzc.blog"); // 设置包名称
        pc.setModuleName(moduleName);  // 若设置后会生成com.progzc.blog/${moduleName}包
        // 生成entity文件夹,设置后会生成com.progzc.blog/${moduleName}/entity/${category}包
        pc.setEntity("entity" + "." + category); 
        // 生成mapper文件夹,设置后会生成com.progzc.blog/${moduleName}/mapper/${category}包
        pc.setMapper("mapper" + "." + category); 
        // 生成service文件夹,设置后会生成com.progzc.blog/${moduleName}/service/${category}包
        pc.setService("service" + "." + category); 
        // 生成service.impl文件夹,设置后会生成com.progzc.blog/${moduleName}/service/impl/${category}包
        pc.setServiceImpl("service.impl" + "." + category); 
        // 生成controller文件夹,设置后会生成com.progzc.blog/${moduleName}/controller/${category}包
        pc.setController("controller" + "." + category); 
        pc.setXml(null); // *Mapper.xml使用自定义配置
        mpg.setPackageInfo(pc); // 添加包配置信息

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 当代码生成器自动生成好代码后，若后续不需再重新生成了，为了防止误操作，应修改为return false
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                return !new File(filePath).exists(); // 若文件存在，则不会重新生成
            }
        });

        // 以下可以自定义配置，但有个两个注意点：
        // 1. new FileOutConfig("/templates/mapper.xml.vm")中的模板文件一定要带后缀.vm，否则会报错。
        // 2. outputFile方法中返回的文件路径中的文件夹需要事先创建好，否则会报错。(这里我们对原程序进行改进)
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") { // 这里一定要带后缀.vm告知使用Velocity进行解析
            @Override
            public String outputFile(TableInfo tableInfo) {
                StringBuilder xmlfilePath = new StringBuilder();
                xmlfilePath.append(projectPath)
                           .append("\\zcblog-backend\\zcblog-core\\src\\main\\resources/mapper\\")
                           .append(category);
                File file = new File(xmlfilePath.toString());
                if(!file.exists() || !file.isDirectory()){
                    file.mkdirs();
                }
                String xmlfileName = tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                return xmlfilePath + "\\" + xmlfileName;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplateEngine(new VelocityTemplateEngine()); // 设置使用Velocity模板引擎

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("templates/entity.java");
        templateConfig.setController("templates/controller.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setXml(null); // *Mapper.xml使用自定义配置
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableName); // 设置表名
        strategy.setLogicDeleteFieldName("deleted"); // 设置逻辑删除
        TableFill create_time = new TableFill("create_time", FieldFill.INSERT); // 设置自动填充
        TableFill update_time = new TableFill("update_time", FieldFill.UPDATE); // 设置逻辑删除
        List<TableFill> fillList = new ArrayList<TableFill>();
        fillList.add(create_time);
        fillList.add(update_time);
        strategy.setTableFillList(fillList);
        strategy.setVersionFieldName("version"); // 设置乐观锁
        mpg.setStrategy(strategy);
        mpg.execute();
    }

    // 自定义TableModule封装tableName、moduleName和category参数
    static class TableModule{
        private String tableName;
        private String moduleName;
        private String category;

        /**
         * 生成的文件：${outputDir}/${parent}/${moduleName}/${fileType}/${category}/文件名
         * @param tableName 表名
         * @param moduleName 模块名
         * @param category 分类
         */
        public TableModule(String tableName, String moduleName, String category){
            this.tableName = tableName;
            this.moduleName = moduleName;
            this.category = category;
        }
    }

    public static void main(String[] args) {
        List<TableModule> list = new ArrayList<TableModule>();
        list.add(new TableModule("article", null, "article"));
        list.add(new TableModule("gallery", null,  "gallery"));
        list.add(new TableModule("encrypt", null, "operation"));
        list.add(new TableModule("tag", null, "operation"));
        list.add(new TableModule("tag_link", null,  "operation"));
        list.add(new TableModule("log_like", null,  "log"));
        list.add(new TableModule("log_view", null,  "log"));
        list.add(new TableModule("oss_resource", null,  "oss"));
        list.add(new TableModule("sys_menu", null,  "sys"));
        list.add(new TableModule("sys_role", null,  "sys"));
        list.add(new TableModule("sys_role_menu", null,  "sys"));
        list.add(new TableModule("sys_user", null,  "sys"));
        list.add(new TableModule("sys_user_role", null,  "sys"));
        list.forEach( e -> codeGenerator(e.tableName, e.moduleName, e.category));
    }
}
```

## 3.2 设计细节

### 3.2.1 避免误操作覆盖源文件

若第一次使用`CodeGeneratorUtils`工具类生成了源文件后，为了避免第二次误操作运行`CodeGeneratorUtils`后覆盖源文件，代码中添加了相应的避免误操作的逻辑。

```java
// 针对entity、controller、service、serviceImpl
gc.setFileOverride(false); // 不覆盖原来文件（否则会比较危险），也可在cfg.setFileCreate中进行自定义配置

// 针对*Mapper.xml
return !new File(filePath).exists(); // 若文件存在，则不会重新生成
```

### 3.2.2 可拓展性

设计了内部类`TableModule`用来封装表名、模块名和分类名。其中表名指定用于自动生成代码的表，模块名和分类名共同结合起来生成指定的包名。结合代码生成器的全局配置，最终生成的目录为：

```html
${outputDir}/${parent}/${moduleName}/${fileType}/${category}
```

### 3.2.3 xml的生成

由于xml一般放置在src/main/resourcess目录下，需要自定义配置文件用于生成*Mapper.xml。由如下几点需要注意：

1. `new FileOutConfig("/templates/mapper.xml.vm")`中指定模板文件时一定要带后缀.vm，**否则会报错找不到模板文件**；而`templateConfig.setEntity("templates/entity.java")`中指定模板文件时不需要带后缀.vm。
2. `outputFile方法`中返回文件路径中的文件夹需要事先创建好，否则会报错（这里我们对程序进行改进，若手动未完成，则交由程序自动创建）。
3. 使用velocity模板引擎而非freemarker模板引擎。
4. 对模板文件进行适当的修改以满足我们的要求。

> 关于velocity模板和freemarker模板的下载，可以在Github或者码云下载MybatisPlus的源代码、模板文件在mybatis-plus-generator/src/main/resources/templates文件夹中。**注意两点**：
>
> - 下载的velocity模板或freemarker模板要与所使用的MyBatisPlus的版本一致。
> - 如何获取旧版本的模板文件？使用git工具将MybatisPlus源码克隆到本地，切换到指定的标签，然会就可以找到旧版本的模板文件了！

### 3.2.4 常见的错误

1. 显示使用的模板文件报错（如下图所示）。有如下几种可能：一是模板文件的语法出错（**由于是开发团队所提供，出错可能性不大**），二是模板文件与MyBatisPlus版本不一致；三是使用的模板引擎不支持该模板文件。

   ![Snipaste_2020-10-25_10-04-20](zcblog-backend-docs.assets/Snipaste_2020-10-25_10-04-20.png)
   
   该错误最终定位为模板引擎不支持模板文件。将模板引擎由freemarker更换为velocity，同时将.ftl模板文件更换为.vm模板文件后解决了问题。

2. 显示找不到指定路径。

   ![Snipaste_2020-10-25_10-39-31](zcblog-backend-docs.assets/Snipaste_2020-10-25_10-39-31.png)
   该错误是由于既未手动创建*Mapper.xml所需的文件目录，也未在程序中自动生成该目录，导致程序在生成文件时出错。严格按照`3.2.3节`所述进行操作可解决该问题。

> 参考博客文章：[Velocity模板引擎语法](https://www.jianshu.com/p/d458d7b8d759)、[代码生成器](https://baomidou.com/guide/generator.html)

# 4 lombok的使用

lombok主要使用注解来简化代码，使代码更加简洁，其使用方法较简单。使用前需IDEA需要安装lombok插件。

## 4.1 基本使用

lombok中的常见注解：

- **@Setter：生成set方法**

> 1. 可以添加访问权限：`@Setter(AccessLevel.PROTECTED)`（默认是PUBLIC权限）。
> 2. 对于boolean类型，生成的set方法是setXxx；对于Boolean类型，生成的set方法是setXxx。

- **@Getter：生成get方法**

> 1. 可以添加访问权限，类似于@Setter。
> 2. 对于boolean类型，生成的get方法是**isXxx**；对于Boolean类型，生成的get方法是getXxx。

- **@Builder：表示该类可以通过builder（建造者模式）构建对象**（非常好用）

> 1. 对属性赋值可以实现链式操作。

- **@RequiredArgsConstructor：生成一个该类的构造函数，禁止无参构造**

> 1. 构造参数只包括**@NonNull**注解的成员变量。

- **@NoArgsConstructor：生成一个无参构造器**

> 1. 使用jackson反序列化对象时，使用无参构造函数创建对象。故而当class会用来序列化未json时，可以使用@NoArgsConstructor来添加一个无参构造函数。
> 2. 不可变类（含有final field）不要使用@NoArgsConstructor注解，否则编译会报错；若使用@NoArgsConstructor(force = true)，那么final的field会初始化为0/false/null（一般不采用这种做法）。
> 3. 当成员变量同时有**@NonNull**注解时，依然可以生成无参构造函数。

- **@AllArgsConstructor：生成包含所有成员变量作为构造参数的构造器**
- **@ToString：重写该类的toString方法**
- **@EqualsAndHashCode：重写该类的equals和hashCode方法**

> 1. equals方法只比较当前类的属性，hashCode也只根据当前类的属性生成。
> 2. 对于父类是Object且使用了`@EqualsAndHashCode(callSuper = true)`注解的类，这个类由 lombok 生成的equals方法只有在两个对象是同一个对象时，才会返回 true ，否则总为 false ，无论它们的属性是否相同。一般应设置`@EqualsAndHashCode(callSuper = false)`。
> 3. 使用@EqualsAndHashCode或@Date时最好不要有继承关系。
> 4. 若自己重写了equals方法或hashCode方法，则lombok不会对显示重写的方法进行生成。
> 5. 同时使用@EqualsAndHashCode和@Date，以@EqualsAndHashCode为准。

- **@Data：等价于@Setter + @Getter + @Builder + @RequiredArgsConstructor + @ToString + @EqualsAndHashCode**

- **@Value：生成一个不可变对象，会为成员变量添加final字段**（不太常用）

> 1. @Value等价于@Getter + @AllArgsConstructor + @ToString + @EqualsAndHashCode。

- @Accessors：存取器，用于配置getter和setter方法的生成结果。

> 1. @Accessors(chain = true)：表示setter方法返回当前对象。（**一般用于级联操作**）
> 2. @Accessors(fluent = true)：表示getter和setter方法的方法名都是基础属性名，且setter方法返回当前对象。（**一般不采用**）
> 3. 使用这个注解后，bean拷贝工具类可能会报错。

> 参考博客文章：**[Lombok中@Data的使用](https://www.cnblogs.com/death00/p/11722152.html)**、**[lombok使用基础教程](https://www.cnblogs.com/woshimrf/p/lombok-usage.html)**、[建造者模式](https://www.jianshu.com/p/3d1c9ffb0a28)

## 4.2 @Document/@Field/@Id

@Document注解是ElasticSearch的注解，可以用于指定索引库的名称、类型、分区、备份数以及刷新间隔。

- 例如：`@Document(indexName = "zcblog", type = "article")`表示索引库的名称是zcblog、类型名称是article；默认分区数量为5，备份数量为1，刷新间隔为1s。
- 需要将@Document与@Documented区分开，@Documented是用来将标记元素的注解信息包含在javadoc中。

@Field：是ElasticSearch的注解

@Id：Spring Date的注解，声明该标记属性为主键。

@JsonIclude：例如：`@JsonInclude(IsonInclude.Include.NON_NULL)`作用在类上，表示只有非空的属性才会参与实例化。

# 5 Swagger的使用

## 5.1 Swagger的介绍

**Swagger的定义**：一款流行的API框架。

**Swagger的作用**：支持自动生成可视化的RESTful API文档；可进行在线测试API，并实现商业API的管理。

**Swagger的应用场景**：主要应用于前后端分离的项目，作为前后端开发工程师进行协同工作；实现类似postman的网络请求测试。

> 参考博客文章：**[B站狂神说Swagger视频](https://www.bilibili.com/video/BV1Y441197Lw)**、[狂神说Swagger](https://mp.weixin.qq.com/s/0-c0MAgtyOeKx6qzmdUG0w)、[Swagger yml完全注释](https://blog.csdn.net/u010466329/article/details/78522992)、[Swagger的介绍](https://blog.csdn.net/weixin_37509652/article/details/80094370)、[Swagger注解](https://blog.csdn.net/chinassj/article/details/81875038)、[添加Header全局配置](https://www.jianshu.com/p/6e5ee9dd5a61)

## 5.2 Swagger的配置

Swagger的配置在`SwaggerConfig.java`中设置：

```java
@Configuration
@EnableSwagger2 // 启用Swagger
public class SwaggerConfig implements WebMvcConfigurer {

    // 加载Swagger的默认U界面
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    // 配置Swagger的Docket的Bean实例（每一个Docket的Bean实例对应于一个分组，这样可以方便协同开发）
    @Bean
    public Docket createRestApiGroup1(Environment environment){
        // 设置要显示的Swagger环境
        Profiles profiles = Profiles.of("dev", "test");
        // 获取项目的环境
        boolean isDevAndTest = environment.acceptsProfiles(profiles);

        System.out.println("isDevAndTest："+isDevAndTest);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否启动Swagger，若为false，则Swagger不能在浏览器中访问
                .enable(isDevAndTest) // 可以控制Swagger在开发及测试环境中使用，在生产环境不使用
                .select()
                // RequestHandlerSelectors.basePackage("包名")：扫描指定的包
                // RequestHandlerSelectors.any()：扫描全部
                // RequestHandlerSelectors.none()：不扫描
                // RequestHandlerSelectors.withMethodAnnotation(注解.class)：扫描方法上的注解
                // RequestHandlerSelectors.withClassAnnotation(注解.class)：扫描类上的注解
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 配置要扫描接口的方式
                // PathSelectors.any()：放行所有路径
                // PathSelectors.ant("/article")：只放行/article路径
                .paths(PathSelectors.any()) // 过滤映射路径
                .build()
                .groupName("Clouds")
                // 可以由使用者设置全局token（一般登录成功后都会设置一个token作为通行证）放置到HTTP请求头中，在跨域访问时作为通行证
                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        // 作者信息
        Contact contact = new Contact("Clouds", "http://blog.progzc.com", "zcprog@foxmail.com");
        return new ApiInfoBuilder()
                .title("zcblog")
                .description("zcblog的接口文档")
                .termsOfServiceUrl("http://blog.progzc.com")
                .version("v1.0")
                .contact(contact)
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

    private List<ApiKey> security() {
        // 设置登录的用户名为token，登录的密码为token
        return newArrayList(new ApiKey("token", "token", "header"));
    }

}
```

**注意事项：**

1. SpringBoot项目中可以通过WebMvcConfigurer对网络请求进行拦截处理、加载资源等。使用Swagger需要加载`swagger-ui.html`这一静态资源（前提是spring.resources.add-mappings设置为false，则需要这一步）。
2. 为了安全以及提高性能，需要控制Swagger在开发及测试环境中使用，但在生产环境中禁用。Docket的enable方法设置为true表示允许访问`swagger-ui.html`，设置为false表示禁止访问；
3. **若application.yml中的spring.resources.add-mappings设置为false，则需要在addResourceHandlers方法中添加指定的静态资源文件这样才能访问`swagger-ui.html`；若为false，则可以不要指定静态资源文件**。
4. 可以在请求头中设置全局token作为登录成功后的通行证，可以解决由于登录权限问题，每次进行API测试都要输入token才能访问接口API的问题。
5. **多人协同开发不同模块，可以采用分组功能。这样每个人的业务API都会在一个分组中，便于查询或与前端人员沟通。**

## 5.3 基本使用

- **@Api：一般作用在类（如Controller）上，用于标记该类作为Swagger文档资源**

> 例如：`@Api(value = "/user", description = "Operations about user")`表示映射路径和描述。

- **@ApiOperation：一般作用在类（如Controller）的方法上**
- **@ApiParam：一般作用在类（如Controller）方法的参数上**
- **@ApiModel：一般给entity类（或者PO/VO/...）添加此注解**

- **@ApiModelProperty：一般给entity类（或者PO/VO/...）的成员变量添加此注解**

> 例如：`@ApiModelProperty(value = "xxx属性说明", hidden = true)`，其中hidden默认为false，若设置为true可以隐藏该属性。

- @ApiResponse：响应配置。

> 例如：`@ApiResponse(code = 400, message = "Invalid user supplied")`表示响应状态码和响应消息。

- @ApiResponses：响应集配置。

> 例如：`@ApiResponses({ @ApiResponse(code = 400, message = "Invalid Order") })`。

- @ResponseHeader：响应头配置。

> 例如：`@ResponseHeader(name="head1",description="response head conf")`。

## 5.4 切换其他UI样式

除了Swagger官方提供的UI样式外，一些第三方也根据Swagger源码开发了其他的更美观友好的UI样式供选用。使用方法也很简单，直接使用第三方的UI Jar包替换掉Swagger官方提供的UI Jar包即可。

```xml
<!--Swagger默认的UI界面-->
<!--默认地址：http://localhost:8080/swagger-ui.html-->
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger-ui</artifactId>
   <version>2.9.2</version>
</dependency>

<!--Bootstrap UI界面-->
<!--默认地址：http://localhost:8080/doc.html-->
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.1</version>
</dependency>

<!--Layui UI界面-->
<!--默认地址：http://localhost:8080/docs.html-->
<dependency>
   <groupId>com.github.caspar-chen</groupId>
   <artifactId>swagger-ui-layer</artifactId>
   <version>1.1.3</version>
</dependency>

<!--mgui UI界面-->
<!--默认地址：http://localhost:8080/document.html-->
<dependency>
   <groupId>com.zyplayer</groupId>
   <artifactId>swagger-mg-ui</artifactId>
   <version>1.0.6</version>
</dependency>
```

# 6 项目热部署

在开发项目时，每次修改完代码都要重新启动项目，会导致开发效率很低。项目实现热部署可以解决这一问题。

## 6.1 项目热部署的两种方式

### 6.1.1 使用devtools工具

使用devtools工具包来进行热部署简单方便，但是有一个缺点：**这种热部署方式会重启项目，清空session中的值；如果有用户登录的话，项目重启后需要重新登录。**

具体步骤：

- 第1步：引入`spring-boot-devtools`依赖（可以在父模块引入版本号，子模块中引入依赖）。

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <version>2.1.6.RELEASE</version>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

- 第2步：在`BlogRunApplication.java`所在子模块的pom.xml中添加插件。

```xml
<build>
	<finalName>Clouds' Blog</finalName>
	<plugins>
		<plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <!--指定全局入口文件-->
            <configuration>
                <mainClass>com.progzc.blog.BlogRunApplication</mainClass>
                <layout>ZIP</layout>
                <fork>true</fork>
                <addResources>true</addResources>
			</configuration>
		</plugin>
	</plugins>
</build>
```

- 第3步：在application.yml中配置开启热部署。

```yaml
spring.devtools.restart.enabled: true
```

- 第4步：在IDEA中设置勾选`Build project automatically`。

![image-20201026210800865](zcblog-backend-docs.assets/image-20201026210800865.png)

- 第5步：按住`ctrl + shift + alt + /`出现如下界面，点击Registry...，然后勾选`compiler.automake.allow.when.app.running`。

![image-20201026211003150](zcblog-backend-docs.assets/image-20201026211003150.png)

![image-20201026211216171](zcblog-backend-docs.assets/image-20201026211216171.png)

- 第6步：在IDEA中设置`Running Application Update Policies`策略为`Update classes and resources`。（**网上的教程缺这一步，不会生效**）。

![image-20201026212416118](zcblog-backend-docs.assets/image-20201026212416118.png)

### 6.1.2 使用Springloaded

前面提到过，`devtools`实现热部署的方式是重启应用，导致会清除清空session中的值；此外，这种热部署更新的方式较慢。使用Springloaded进行热部署时不会重启应用，可以保证session中的值不会被清除，但是SpringLoaded对于方法内修改代码时热部署可以生效，**增加方法时热部署却不能生效(即使采用Debug模式启动也不行)**。

具体步骤：

- 第1步：下载`springloaded-1.2.8.RELEASE.jar`到本地（下载方式有：Mavon下载到本地仓库、mvnrepository.com网站直接下载、Github进行下载）。
- 第2步：在IDEA中进行配置，使得在启动项目时在jvm命令中增加：`-javaagent:springloaded-1.2.8.RELEASE.jar包本地绝对路径 -noverify`。

![image-20201026230320973](zcblog-backend-docs.assets/image-20201026230320973.png)

> 参考博客文章：[ spring-boot-devtools实现热部署](https://www.cnblogs.com/zhukf/p/12672180.html)、[Springloaded实现热部署](https://blog.csdn.net/tang86100/article/details/78772079)

# 7 hibernate-validator参数校验

## 7.1 来源介绍

项目开发的时候，基本上每个接口都要对参数进行校验，比如一些格式检验、非空校验。若参数比较多，代码中就会出现大量的if...else...，这样会让代码看起来很复杂，本质上处理业务逻辑的代码不容易显现处理，造成代码主次不分、冗余度过高的问题。

幸好的是Bean Validator（Java定义的一套基于注解的数据校验规范）可以帮我们解决这一难题。Bean Validator的校验规范版本有JSR 303（V1.0版本）、JSR 349（V2.0版本）、JSR 380（V3.0版本）。目前Springboot的`starter-web`中已经集成了基于JSR检验规范的`hibernate-validator`（没错，`hibernate-validator`就来自于曾经大名鼎鼎的持久层框架Hibernate）。**注意：javax.validation是一个基于JSR的接口规范，而hibernate-validator是其的一种具体实现。**

![image-20201027084516470](zcblog-backend-docs.assets/image-20201027084516470.png)

## 7.1 基本使用

### 7.1.1 使用场景

**根据项目架构选择在哪个业务层进行参数校验**：

- 针对普通的应用：一般在Controller层做检验。
- 针对RPC应用：一般在Service层做检验。
- **不仅可以对参数进行验证，还可以对返回值进行验证。**

```yaml
# 1. 在Controller层做验证
  # 1.1 首先在Controller类上添加@Validated注解。
  # 1.2 然后在COntroller类的方法参数上添加@Valid注解（可去掉1.1的@Validated注解，详见@Valid与@Validated的区别）。
  # 1.3 最后给方法参数添加指定的校验方式(如@NotNoll、@NotBlank...);若在entity类的成员变量中指定的话，则对所有使用的ServiceImpl层均生效。
  
# 2. 在ServiceImpl层做验证
  # 1.1 首先在ServiceImpl类上添加@Validated注解。
  # 1.2 然后在ServiceImpl类的方法参数上添加@Valid注（可去掉1.1的@Validated注解，详见@Valid与@Validated的区别）。
  # 1.3 最后给方法参数添加指定的校验方式(如@NotNoll、@NotBlank...);若在entity类的成员变量中指定的话，则对所有使用的ServiceImpl层均生效。
  
# 3. 在Service层（接口）做验证（这里要注意与前两者的区别）
  # 1.1 首先在Service类的方法参数上添加@Valid注解
  # 1.2 然后在ServiceImpl类上添加@Validated注解（若在Service类上添加@Validated注解，则对Service的所有实现类都生效）。
  # 1.3 最后给方法参数添加指定的校验方式(如@NotNoll、@NotBlank...);若在entity类的成员变量中指定的话，则对所有使用的Service层均生效。
  
# 4. 对返回值添加注解：与方法参数校验类似，只是作用在返回值上。
```

### 7.1.2 使用注解

- @NotBlank：不能为null，并且长度大于0（**只能用于String上面不能为null，调用trim()后，长度必须大于0**）
- @NotNull：不能为null（**适用于任何类型被注解的元素必须不能为null**）
- @NotEmpty：不能为null且@Size(min = 1)（**适用于String Map或者数组不能为null且长度必须大于0**）
- **@Valid和Validated的区别：**

> 1. @Validated是@Valid 的一次封装，是Spring提供的校验机制使用。
> 2. @Valid和@Validated均用于校验，但作用地方有区别。**@Valid作用在方法、字段、构造器和参数上；而@Validated作用在类、方法和参数上。**
> 3. @Valid可进行**级联校验（即嵌套校验）**，而@Validated却不支持级联校验。
> 4. @Valid不提供分组功能；而@Validated提供分组功能（可实现**分组检验**、**按组序列校验**、**同时校验多个参数**）。
> 5. 针对**有接口的实现类的方法参数添加校验**时，应该在接口的方法参数上添加@Valid注解，在接口的实现类的方法参数上添加注解会报错（**详见7.1.1使用场景3**）。

### 7.1.3 级联及一对多检验

级联即一对一验证。级联及一对多验证采用@Valid注解。

```java
// 举个例子
public Class Employee {
    ... 
    // 级联(一对一)验证
    @Valid 
    private Department department; // 员工与部门一对一
    // 一对多验证
    private List<@Valid Project> projects; // 员工与项目一对多
    ...
} 
```

### 7.1.4 规范异常信息

将异常信息（由**状态码+消息**组成）封装成枚举类，这样便于统一管理。**异常信息遵循RETSTful原则**。

将异常信息封装到`ErrorEnum.java`枚举类中：

```java
public enum ErrorEnum {
    // 系统错误
    UNKNOWN(500,"系统内部错误，请联系管理员"),
    PATH_NOT_FOUND(404,"路径不存在，请检查路径"),
    NO_AUTH(403,"没有权限，请联系管理员"),
    DUPLICATE_KEY(501,"数据库中已存在该记录"),
    TOKEN_GENERATOR_ERROR(502,"token生成失败"),
    NO_UUID(503,"uuid为空"),
    SQL_ILLEGAL(504,"sql非法"),

    //用户权限错误
    INVALID_TOKEN(1001,"token不合法"),

    //登录模块错误
    LOGIN_FAIL(10001,"登录失败"),
    CAPTCHA_WRONG(10002,"验证码错误"),
    USERNAME_OR_PASSWORD_WRONG(10003,"用户名或密码错误"),

    //七牛云OSS错误
    OSS_CONFIG_ERROR(10050,"七牛云配置信息错误"),
    OSS_UPLOAD_ERROR(10051,"Article图片上传失败");

    private int code;
    private String msg;
}
```

### 7.1.5 处理异常

为了使参数校验后返回给前端的结果更加优雅，需要自定义全局异常处理器（**这个全局处理异常不仅仅处理参数校验返回的异常，还可以对项目中出现的其他异常进行处理**）。

在`MyExceptionHandler.java`中对全局异常进行处理：

```java
// @ControllerAdvice有三种功能：处理全局异常、绑定全局数据、预处理全局数据。（本项目用到的是第一种功能）
@RestControllerAdvice // 相当于@ResponseBody+@ControllerAdvice表示处理全局异常,且返回字符串
@Slf4j
public class MyExceptionHandler { // MyExceptionHandler用于处理全局异常
    // 处理自定义异常（包含校验异常）
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e){
        Result result=new Result();
        result.put("code",e.getCode());
        result.put("msg",e.getMsg());
        return result;
    }
    
    // 处理Spring中的404异常
    // 需要在yml配置文件中设置：spring.mvc.throw-exception-if-no-handler-found: true  #出现错误时, 直接抛出异常
    // 需要在yml配置文件中设置：spring.resources.add-mappings: false  #关闭工程中的静态资源映射所以此时需要访问swagger-ui.html就必须要在Swagger配置文件中添加资源路径
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handlerNoFoundException(Exception e){
        log.error(e.getMessage(),e);
        return Result.exception(ErrorEnum.PATH_NOT_FOUND);
    }

    // 处理向数据库中插入数据时出现的异常
    // 继承关系：DuplicateKeyException-->DataIntegrityViolationException-->NonTransientDataAccessException-->DataAccessException-->NestedRuntimeException-->RuntimeException-->Exception
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(),e);
        return Result.exception(ErrorEnum.DUPLICATE_KEY);
    }

    // 处理登录与鉴权中出现的异常
    @ExceptionHandler(AuthorizationException.class)
    public Result handleAuthorizationException(AuthorizationException e){
        log.error(e.getMessage(),e);
        return Result.exception(ErrorEnum.NO_AUTH);
    }

    // 处理其他异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error(e.getMessage(),e);
        return Result.exception();
    }
    
   // 可以在这里直接对校验异常进行处理（但是本项目并未采用这种方式，本项目中参数校验的异常是在自定义校验工具类ValidatorUtils中进行处理，转成自定义异常然后再在MyExceptionHandler中进行处理的）
   ...
 
}
```
**注意事项：**可以在这里MyExceptionHandler.java对校验异常进行直接处理（但是本项目并未采用这种方式）；本项目中参数校验的异常是在自定义校验工具类ValidatorUtils中进行处理的），这是因为本项目并未使用@Valid或@Validated注解自动将参数校验交由Spring处理；而是主动在ValidatorUtils中进行参数校验和处理异常（转为自定义异常，这种灵活性更大）。

在`ValidatorUtils.java`中处理校验异常（校验若不通过就抛出MyException异常）：

```java
public class ValidatorUtils {
    private static Validator validator;
    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws MyException  校验不通过，则报MyException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws MyException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            List<String> collect = constraintViolations.stream().map(constant -> constant.getMessage()).collect(Collectors.toList());
            String msg = StringUtils.join(collect, ",");
            throw new MyException(msg);
        }
    }
}
```
自定义异常`MyException.java`：

```java
public class MyException extends RuntimeException{
    private String msg;
    private int code = 500; // 默认500异常

    public MyException(){
        super(ErrorEnum.UNKNOWN.getMsg());
        this.msg=ErrorEnum.UNKNOWN.getMsg();
    }
    
    public MyException(ErrorEnum eEnum,Throwable e){
        super(eEnum.getMsg(),e);
        this.msg=eEnum.getMsg();
        this.code=eEnum.getCode();
    }

    public MyException(ErrorEnum eEnum){
        this.msg=eEnum.getMsg();
        this.code=eEnum.getCode();
    }

    public MyException(String exception){
       this.msg=exception;
    }
}
```

> 参考博客文章：[关于NoHandlerFoundException异常](https://blog.csdn.net/qq_36666651/article/details/81135139)

### 7.1.6 分组校验

有这样的场景：需要在插入/更新时保证某个字段不为空；查询时却没有这个要求。那么分组校验就可以很好地解决这个问题了。

分组校验的步骤：

```java
// 1. 定义两个接口类（不需要有任何内容）
public interface AddGroup {
   
}
public interface UpdateGroup {

}

// 2. 在成员变量的校验注解参数上添加指定分组
@NotBlank(message="博文内容不能为空", groups = {AddGroup.class, UpdateGroup.class})
private Employee employee  // 在entity类中

// 3. 在Controller层/Service层/ServiceImpl层做校验
@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController{
    @PutMapping("/update")
    public Result update(@RequestBody @Validated({AddGroup.class}) Employee employee){
        ...
        return new Result();
        ...
    }
}
```

**注意事项：**

- **如果指定了验证组，那么该参数只属于指定的验证组**；未指定验证组的属性属于默认组。
- 也可以自定义检验工具类控制校验的颗粒度。（**本项目采用这种方式**）

**特别提示：**关于参数校验的高级用法（如自定义注解、list中做分组检验、bean参数间的逻辑校验，本项目并未使用到。可以需要时再去学习...）

> 参考博客文章：**[B站Hibernate Validator参数校验视频](https://www.bilibili.com/video/BV1UE411t7BZ)**、**[SpringBoot参数校验](https://www.cnblogs.com/mooba/p/11276062.html)**、[@NotBlank/@NotNull/@NotEmpty](https://www.cnblogs.com/xinruyi/p/11257663.html)、[@Valid与@Validated的区别](https://blog.csdn.net/gaojp008/article/details/80583301)、[@Valid与@Validated总结](https://www.cnblogs.com/javastack/p/10297550.html)、[深入了解数据校验](https://cloud.tencent.com/developer/article/1497733)、[@ControllerAdvice的三种功能](https://www.cnblogs.com/lenve/p/10748453.html)

# 8 封装响应结果

对于网络请求，将响应数据封装到HashMap中，主要包括三个部分**状态码、消息和数据**。

在`Result.java`中封装响应结果：

```java
public class Result extends HashMap<String, Object> {

    public Result() {
        put("code", 200);
        put("msg", "success");
    }

    public static Result ok() {
        return new Result();
    }

    public static Result error() {
        return error(ErrorEnum.UNKNOWN);
    }

    public static Result error(ErrorEnum eEnum) {
        return new Result().put("code", eEnum.getCode()).put("msg", eEnum.getMsg());
    }

    public static Result error(String msg) {
        return new Result().put("msg",msg).put("code", ErrorEnum.UNKNOWN.getCode());
    }

    public static Result error(Integer code , String msg){
        return new Result().put("code",code).put("msg",msg);
    }

    public static Result exception() {
        return exception(ErrorEnum.UNKNOWN);
    }

    public static Result exception(ErrorEnum eEnum) {
        return new Result().put("code", eEnum.getCode()).put("msg", eEnum.getMsg());
    }
    
    /**
     * 封装业务数据
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this; 
    }
}
```

# 9 跨域配置

本博客项目采用前后端分离的形式，前端访问后端数据存在跨域访问的问题。

WebMvcConfigurer是Springboot内部的一个配置接口，用来代替传统xml的配置文件，可以自定义一些Handler、Interceptor、ViewResolver、MessageConverter。继承WebMvcConfigurer接口后常用的一些配置方法是：addInterceptors方法（配置拦截器）、addViewControllers方法（配置页面跳转）、**addResourceHandlers方法**（配置静态资源访问，例如Swagger的`swagger-ui.html`的页面访问就是在SwaggerConfig的addCorsMappings方法中进行配置的）、**addCorsMappings方法**（配置跨域）

> 跨域问题：CORS是一个W3C标准，全称是"跨域资源共享"（Cross-origin resource sharing）。它允许浏览器向跨源服务器，发出XMLHttpRequest请求，从而克服了Ajax只能同源使用的限制。

在`CorsConfig.java`中进行跨域的配置：

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 表示对所有发往控制器的请求都放行
                .allowedOrigins("*") // *表示对所有的地址都可以访问
                .allowCredentials(true) //可以携带cookie，最终的结果是可以 在跨域请求的时候获取同一个 session
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }
}
```

> 参考博客文章：[WebMvcConfigurer详解](https://blog.csdn.net/zhangpower1993/article/details/89016503)、[Spring官网doc](https://docs.spring.io/spring-framework/docs/5.2.9.RELEASE/spring-framework-reference/web.html#mvc-cors)、[@CrossOrigin解决跨域问题](https://blog.csdn.net/testcs_dn/article/details/86537605)

# 10 Shiro完成登录与鉴权



# 11 Spring 缓存

在项目开发过程中，可以使用Spring缓存技术将数据存入服务器的缓存中（**本质上缓存是在Spring的容器中，位于服务器的内存上**），这样对于一些重复的查询操作可以避免频繁访问数据库，提高响应速度。

## 11.1 使用缓存的步骤

使用Spring缓存（保证Springd的版本高于V3.1）的步骤如下：

1. 第1步：在Springboot**启动类**或者**某一配置类**上使用**@EnableCaching**注解启用Spring缓存技术。
2. 第2步：在类上或类中的方法上（**该类必须注入到容器中，否则缓存不会生效**）使用缓存注解（例如：@Cacheable添加缓存、@CacheEvict清除缓存...）。

## 11.2 缓存注解

- **@EnableCaching**：开启缓存。在项目启动类或某个配置类上使用此注解后，则表示允许使用注解的方式进行缓存操作。
- **@CacheEvict**：可用于类或方法上。在执行完目标方法后，清除缓存中对应key的数据（如果缓存中有对应key的数据缓存的话）。
- **@Cacheable**：可用于类或方法上。在目标方法执行前，会根据key先去缓存中查询看是否有数据，若存在就直接返回缓存中的key对应的value值。不再执行目标方法；若不存在则执行目标方法，并将方法的返回值作为value，并以键值对的形式存入缓存。
- **@CachePut**：可用于类或方法上。在执行完目标方法后，并将方法的返回值作为value，并以键值对的形式存入缓存中。
- @Caching：可作为@Cacheable、@CacheEvict、@CachePut三种注解中的的任何一种或几种来使用。
- **@CacheConfig**：@Cacheable、@CacheEvict、@CachePut这三个注解的cacheNames属性是必填项（或value属性是必填项，因为value属性是cacheNames的别名属性）；如果上述三种注解都用的是同一个cacheNames的话，那么在每次都写cacheNames的话，就会显得麻烦。@CacheConfig注解就是来配置一些公共属性（如：cacheNames、keyGenerator等）的值。该注解一般用于类上。

## 11.3 缓存注解的常用属性

- **key属性**：默认/keyGenerator生成/主动指定。**（优先级：主动指定>keyGenerator>默认）**

> 默认key：
>
> 1. 方法无参时，默认key为SimpleKey[]；
> 2. 方法只有一个参数时，默认key为传入的参数的toString的值；
> 3. 方法有多个参数时，默认的key为SimpleKey [参数1的toString的值,参数2的toString的值...]。
>
> keyGenerator生产key：
>
> 1. 编写配置类（配置类需继承CachingConfigurerSupport类，重写keyGenerato()方法），定制化key生成器。
>
> 主动指定key：
>
> 1. 若key为常量，需要使用导引号引起来；
> 2. 也可以使用Spring表达式语言（SpEL）动态为key设置值（可以通过"#形参名"或"#p参数索引"或"#a参数索引"）；
> 3. 可以通过打点的方式对获得的参数进行方法或属性调用（如key="#str.hashCode()"或key="#p1.name"）；
> 4. SpEL中可以是以Spring提供的隐藏根对象，如key="#root.target"表示以全限定类型@内存地址作为key值。

- **condition属性**：若condition结果为true，则注解生效，否则注解不生效（condition的作用时机在缓存注解检查缓存中是否有对应key-value 之前）。

- **cacheNames属性**：命名空间（指开辟的一块内存空间），与value属性互为别名（作用一致）。

> 1. 不同cacheNames下可以有相同的key。
> 2. 若cacheNames（或value）指定了多个命名空间，当进行缓存存储时会在每个命名空间下均存有一份key-value；当进行缓存读取时，会按照cacheNames值里命名空间的顺序，依次从命名空间中查找对应的key，查到即返回key对应的value值。

- **unless属性**：是否令注解（在方法执行后的功能）不生效。若unless结果为true，则不生效；若unless结果为false，则生效。unless默认值为false。

> 1. unless的作用时机在目标方法运行后；若因为直接从缓存中获取到了数据，而导致目标方法没有被执行，那么unless字段不生效。
> 2. unless的作用时机是在方法运行完毕后，所以我们可以用SpEL表达式#result 来获取方法的返回值。

- **allEntries属性**：主要出现在@CacheEvict注解中，表示是否清除指定命名空间中的所有数据，默认为false（即不清除所有数据）。
- **beforeInvocation属性**：主要出现在@CacheEvict注解中，表示是否在目标方法执行前使此注解生效， 默认为false（即不生效）。

> 参考博客文章：[Spring缓存注解](https://blog.csdn.net/justry_deng/article/details/89283664)、[B站Spring缓存视频](https://www.bilibili.com/video/BV1ZE411J7Yb?from=search&seid=4715812049534535936)

# 12 Redis的使用

## 12.1 背景

### 12.1.1 互联网项目的架构演进

**架构模型的演进**：单机MySQL—>Memcached + MySQL读写分离（垂直拆分）—> 分库分表+ MySQL集群（水平拆分）—>负载均衡 + 分库分表 + MySQ集群（水平拆分）+ 图片/文件/流媒体...服务器

MySQL引擎：以前采用MyISAM（表锁）、现在采用Innodb（行锁）。

NoSQL（Not only SQL）特点：

1. 数据之间没有关系，方便扩展。
2. 大数据量高性能（Redis每秒写8万次，每秒读11万次）。
3. 数据类型多样型（不需要事先设计数据库，随取随用）。

大数据的3V+3高：数据3V（海量Volume、多样Variety、实时Velocity）；程序要3高（高并发、高可拓、高性能）。

### 12.1.2 NoSQL的四大分类

- KV键值对：新浪（**Redis**）、美团（Redis+Tair）、阿里/百度（Redis + Memcache）。
- 文档型数据库：**MongoDB**（是一个介于关系型数据库和非关系型数据库的中间的产品）、ConthDB。
- 列存储数据库：**HBase**、分布式文件系统。
- 图形关系数据库：**Neo4J**、InfoGrid，主要用于社交网络、推荐系统。

### 12.1.3 Redis介绍

Redis（Remote Dictionary Server）：远程字典服务。

**Redis的作用**：内存存储、持久化（rdb和aof）；**高速缓存**；发布订阅系统；地图信息分析；计时器、计数器（**浏览量**）...

> 本项目使用Redis做**高速缓存**及使用Hyperloglog统计博客文章**浏览量**、统计页面**访客数**。

**Redis的特性**：数据类型多样化；持久化；集群；事务...

**Redis的基本指令**（指令不区分大小写，但key-value区分大小写）：

- SELECT 3：切换到第3个数据库（Redis默认有16个数据库，默认使用的是第0个）。
- DBBASE：查看数据库大小。
- KEYS pattern：查看满足pattern正则表达式的key。
- FLUSHDB：清空当前数据库。
- FLUSHALL：清空全部数据库。

**为什么Redis这么块**：Redis是单线程的（CPU不是Redis的性能瓶颈，机器的内存和网络带宽才是）。

1. 误区一：高性能的服务器一定是多线程的？（错误）

2. 误区二：多线程（CPU上下文切换也很耗时）一定比单线程效率高（错误）

   原因：Redis是将所有的数据全部存放在内存中，使用单线程操作效率最高；多线程存在CPU上下文切换（很耗时）。因此，对于内存系统而言，没有上下文切换效率更高。

## 12.2 基本使用

### 12.2.1 常用的命令

命令的使用需要在Redis官网或者菜鸟网站上查询即可。

- EXISTS key：判断某个键是否存在。
- MOVE key：移除某个键。
- EXPIRE key seconds：设置某个键的存货时间（单位是秒）。
- TTL key：查看某个键的剩余存活时间。
- SET key value：设置键值对。
- GET key：获取键位key的值。
- TYPE key：查看某个key的数据类型。

### 12.2.2 五种基本数据类型

五大基本数据类型是**String**、**List**、**Set**、**Hash**、**Zset**

- String（字符串）

```yaml
APPEND key value  # 在原来的值上拼接字符串(若key不存在，则相当于新建一个key-value)
STRLEN key  # 获得字符串的长度
incr key / decr key  # 自增1/自减1
INCRBY key num / DECRBY key num  # 自增num/自减num
GETRANGE key start end  # 截取字符串(GETRANGE key 0 -1可以截取全部字符串)
SETRANGE key offset value  # 替换(从offset索引处开始替换，替换的长度位value的长度)
SETEX key seconds value  # 设置过期时间
SETNX key value  # 若key不存在再设置key-value(在分布式锁中会常常使用);若存在则创建失败
mset key value [key value...]  # 设置多个key-value
mget key [key...]  # 获取多个key
msetnx key value [key value...]  # 不存在再设置key-value(原子性的操作:要么一起成功,要么一起失败)
getset key value  # 若存在就获取该key对应的value;若不存在就返回nil,再设置key-value

# 使用场景:
# 1. 计数器
# 2. 统计多单位的数量
# 3. 粉丝数
# 4. 对象缓存存储
```

- List（链表）：可以把List当成栈、队列、阻塞队列

```yaml
LPUSH key value [value...]  # 将一个值或者多个值插入到链表的头部(左)
RPUSH key value [value...]  # 将一个值或者多个值插入到链表的尾部(右)
LRANGE key start stop  # 截取哪几个value（LRANGE key 0 -1 截取链表的所有元素）（不会改变链表）
LPOP key  # 移除链表的头部元素(第一个元素)
RPOP key  # 移除链表的尾部元素(最后一个元素)
LINDEX key index  # 获取链表中索引位index(index从0开始)的元素
LLEN key  # 返回链表的长度
LREM key count value  # 移除链表中指定个数的value元素，精确匹配
LTRIM key start stop  # 通过下表截取指定的长度（会改变链表）
RPOPLPUSH source destination  # 移除源链表的尾部元素，并添加到目标链表的头部元素中
LSET key index value  # 改变链表中索引位index的元素的值，设置为value(要求链表必须先存在,若不存在会报错)
LINSERT key BEFORE|AFTER pivot value  # 在链表中哪个元素之前/或之后插入一个元素

# 使用场景：
# 1. 消息排队
# 2. 消息队列
# 3. 栈
```

- Set（集合）：Set中的值不能重复

```yaml
SADD key value  # 添加一个元素到Set集合中
SMEMBERS key  # 查看Set集合中的所有元素
SISMEMBER key value  # 判断某个元素是否在Set集合中
SCARD key  # 获取Set集合中的元素个数
SREM key value  # 移除Set集合中的某个元素
SRANDMEMBER key [count]  # 随机抽取出count个元素(若未设置count,则随机抽取出1个元素)
SPOP key  # 随机删除Set集合中的一个元素
SMOVE source destination member  # 将source集合中的member元素移除，并添加到destination集合中
SDIFF key1 key2  # 以key1集合为标准，比较两个集合的差集
SINTER key1 key2  # 比较两个集合的交集（如查找共同好友）
SUNION key1 key2  #  求两个集合的并集

# 使用场景：
# 1. 共同关注、共同爱好、推荐好友
```

- Hash（哈希）：key的value值是Map集合，相当于是key-Map

```yaml
HSET key field value  # 在hash表中设置一个field-value
HGET key field  # 获取hash表的一个字段值
HMSET key field value [field value...]  # 在hash表中设置多个field-value
HMGET key field [field...]  # 获取hash表的多个字段值
HGETALL key  # 获取hash表的field-value
HDEL key field  # 删除hash表中的某个字段
HLEN key  # 获取hash表的字段数量
HEXISTS key field  # 判断hash表中的某个字段是否存在
HkEYS key  # 获取hash表中所有字段
HVALS key  # 获取hash表中所有字段值
HINCRBY key field num  # 使hash表中的字段值自增num
HDECRBY key field num  # 使hash表中的字段值自减num
HSETNX key field value  # 若hash表中某个字段不存在，则设置字段值；若存在，则设置失败

# 使用场景：
# 1. Hash一般用于存储需要经常变动的信息（如用户信息）
# 2. Hash更适合于对象的存储，String更加适合于字符串的存储
```

- Zset（有序集合）

```yaml
ZADD key score member [score member...]  # 添加一个或多个元素到有序集合中，并为元素设置score
ZRANGE key start end  # 截取部分元素（如ZRANGE key 0 -1表示截取所有元素）
ZRANGEBYSCORE key min max  # 显示score值在min到max之间的member
ZRANGEBYSCORE key min max WITHSCORES  # 显示score值在min到max之间的member,并附带score值
ZREM key member  # 移除有序集合中的某个元素
ZCARD key  # 获取有序集合中的元素个数
ZREVRANGE key start stop  # 有序集合中索引从start到stop的元素按照从大到小排序
ZCOUNT key min max  # 有序集合中score值在min到max之间的元素数量

# 使用场景：
# 1. 主要用在需要排序的场景：如排行榜、取TOP N测试...
```

### 12.2.3 三种特殊数据类型

- Geospatial（地理位置）

```yaml
# 规则：两级无法直接添加（有效的经度从-180度到180度，有效的纬度从-85.05112878度到85.05112878度）
# 一般会下载城市数据，直接通过Java程序导入
GETADD key longitude latitude member [longitude latitude member...]  # 将经度、纬度、城市名称添加到地理空间中
GEOPOS key member  # 获取指定名称的经纬度信息
GEODIST key member1 member2 km  # 获取两个城市之间的直线距离，指定距离单位是km（默认单位即是km）
# 以给定的经纬度为中心，找出某一半径(单位是km)内的元素
# WITHDIST表示显示到中心的距离、WITHCOORD表示显示他人的定位信息、COUNT num表示只显示num个结果
GEORADIUS key longitude latitude radius km [WITHDIST] [WITHCOORD] [COUNT num] 
# 以给定的城市为中心，找出某一半径(单位是km)内的元素
GEORADIUSBYMEMBER key member radius km
GEOHASH key member1 member2  # 将二维的经纬度转换为一维的字符串（如果两个字符串越接近，那么则距离越近）

# GEO底层的实现原理其实就是ZSet，可以使用Zset命令来操作GEO
ZRANGE key 0 -1  # 查看地图中的所有元素
ZREM key member  # 移除地图中的某个元素


# 使用场景：
# 1. 朋友的定位
# 2. 附近的人
# 3. 打车距离计算...
```

- Hyperloglog（基数统计）

Hyperloglog基数统计的优点：占用的内存固定，只需要12KB内存（2），从内存角度来比较的话使用Hyperloglog作为统计计数是首选。

Hyperloglog基数统计的错误率：0.81%（**针对于统计UV任务，可以忽略不计**）

```yaml
PFADD key element [element...]  # 添加元素到key中
PFCOUNT key  # 统计key中元素的数量
PFMERGE destkey sourcekey [sourcekey...]  # 合并sourcekey1和sourcekey2中的元素（去掉重复值，即并集）

# 使用场景：
# 1. 统计网页的UV(一个人访问一个网站多次，但是还是算作一个人)
# 1.1 传统的解决方式：使用Set保存用户的id（IP地址），然后统计set中的元素数量作为标准判断！但弊端是会保存大量的用户id（IP地址），会造成资源的浪费，而我们的目的是为了计数，而不是保存用户的id。
# 1.2 现在采用Redis Hyperloglog基数统计的方式（如果允许容错）就可以更加优雅地解决我们的问题。
```

- Bitmaps（位图）：操作二进制位来进行记录，只有0和1两个状态（查询的时间复杂度：O(1)）

```yaml
SETBIT key offset value  # 对key所储存的字符串值,设置或清除指定偏移量上的位(bit)
GETBIT key offset  # 对key所储存的字符串值,获取指定偏移量上的位(bit)
BITCOUNT key  # 统计给定字符串中,被设置为1的比特位的数量

# 使用场景：
# 1. 使用位储存统计用户信息（如活跃/不活跃；登录/未登录...）
# 2. 统计365天打卡信息（某天是否打卡、总的打卡天数...）
```

## 12.3 事务

### 12.3.1 事务的处理

**Redis事务的本质**：一组命令（如多个set命令）的集合。一个事务中的所有命令都会被序列化，在事务执行的过程中，会按照顺序执行。

**Redis事务的特性**：一次性、顺序性、排他性。

> 注意事项：
>
> 1. Redis事务没有隔离级别的概念（需回顾Spring中的隔离级别）。
> 2. 所有的命令在事务中，并未被直接执行，只有发起执行命令（EXEC）的时候才会执行。
> 3. **Redis单条命令是原子性的，但是Redis事务不是原子性的。**
>    - 开启（MULTI）事务后，若将命令加入队列时报错（这种错误是**编译型错误**）；执行（EXEC）事务后，所有入队的命令都不会执行。
>    - 开启（MULTI）事务后，若将命令加入队列时成功；执行（EXEC）事务后，部分入队的指令出错（这种错误是**运行时错误**），则未出错的命令依旧会执行成功（**体现了Redis的事务不是原子性的**）。
> 4. 虽然Redis自带的事务并不具备原子性，但是我们可以在java程序中通过异常处理来回滚（详见`12.4.1`）。

**Redis事务的处理**：开启事务（MULTI）-->命令入队（...）-->（EXEC）

```yaml
# 开启事务
MULTI
# 取消事务
DISCARD
# 执行事务
EXEC
```

### 12.3.2 悲观锁/乐观锁

**悲观锁**：很悲观，认为在什么时候都会出问题，无论做什么都会加锁。

**乐观锁**：很乐观，认为什么时候都不会出问题，所以不会上锁。更新数据的时候去判断一下，在此期间是否有人修改过这个数据（先获取version，更新的时候比较version）。

**使用watch可以当作Redis的乐观锁操作，使用unwatch可以取消监视。**

```yaml
# 举个例子
# 步骤一：线程1
watch money  # 监视money（类似于添加乐观锁）
multi  # 开启事务
DECRBY money 20
INCRBY out 20 # 为了模拟多线程操作，先不提交线程1的事务
# 步骤二：线程2
INCRBY money 10  # 线程2修改了money的值
# 步骤三：提交线程1的事务
EXEC  # 执行后输出（nil），表示线程1的事务执行失败了，这是由于watch监视到了线程2对money的改动，起到了乐观锁的作用

# 使用unwatch可以取消监视
# 结合使用watch和unwatch可以实现自旋锁的功能
```

## 12.4 操作Redis

### 12.4.1 使用Jedis操作Redis

使用步骤如下：

```java
// 1. 导入依赖包
...
// 2. 使用Jedis操作Redis（这里截取部分关键代码）
    Jedis jedis = New Jedis("127.0.0.1", 6379);
	// jedis的所有命令就是之前学习客户端操作的哪些指令
	System.out.println(jedis.ping()); // 输出PONG表示已连接上
	// 其他指令不一一讲解
	...
// 3. 使用jedis操作Redis事务
// 注意事项：虽然Redis自带的事务并不具备原子性，但是我们可以在程序中通过异常处理来回滚
public class TestMulti {
    public static void main(String[] args) {
        //创建客户端连接服务端，redis服务端需要被开启
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.flushDB();

        JSONObject jsonObject = new JSONObject(); // 此处需要重点回顾Jackson与FastJson
        jsonObject.put("hello", "world");
        jsonObject.put("name", "java");
        Transaction multi = jedis.multi(); //开启事务
        String result = jsonObject.toJSONString();
        try{
            multi.set("json", result); //向redis存入一条数据
            multi.set("json2", result); //再存入一条数据
            int i = 100/0; //这里引发了异常，用0作为被除数
            multi.exec(); //如果没有引发异常，执行进入队列的命令
        }catch(Exception e){
            e.printStackTrace();
            multi.discard(); // 如果出现异常，回滚(放弃事务)
        }finally{
            System.out.println(jedis.get("json"));
            System.out.println(jedis.get("json2"));
            jedis.close(); //最终关闭客户端
        }
    }
}
```

### 12.4.2 使用Springboot操作Redis

**注意**：在SpringBoot2.x之后，原来使用的JEDIS被替换成了Lettuce。

**原因**：Jedis采用的是直连，多个线程操作的话，是不安全的；若为了避免不安全，使用Jedis pool连接池，又会造成性能的降低（类似BIO模式）。而Lettuce采用Netty，实例可以在多个线程中共享，不存在线程不安全的情况，可以减少线程数据，提高性能（类似NIO模式）。

`RedisAutoConfiguration.java`中的部分源码分析：

```java
	@Bean
    @ConditionalOnMissingBean(name = {"redisTemplate"}) // 可以自定义一个redisTemplate来替换这个默认的Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        // 默认的RedisTemplate没有过多的设置，Redis对象都是需要序列化的！
		// 两个泛型都是Object、Object的类型，我们后面使用需要强制转换<String, Object>
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean // 由于String是Redis中最常使用的类型，所以单独封装了一个stringRedisTemplate Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
```

使用步骤下：

```java
// 1. 导入依赖
// 2. 配置连接
// 3. 测试或使用
//   3.1 实际使用时，一般会根据Spring提供的redisTemplate进行自定义（例如默认的RedisTemplate采用的时JDK序列化的方式，这种方式中文在Redis客户端中的显示不友好（会乱码），一般会自定义使用Jackson或者fastjson进行序列化）。
// 	 3.2 值得注意的是，Jackson或者fastjson进行序列化需要Entity类实现Serializable接口。
//   3.3 除此之外，一般会封装一个RedisUtils工具类。
```

#### 12.4.2.1 自定义redisTemplate

在`RedisConfig.java`中配置自定义redisTemplate Bean：

```java
@Configuration
@EnableCaching // 开启Spring缓存注解
public class RedisConfig {
  
  // 配置Spring缓存管理器
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
			// 未配置的 key 的默认一周过期
			RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(7))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new GenericJackson2JsonRedisSerializer())),this.getRedisCacheConfigurationMap());
  }
  private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(1);
        // 文章的缓存默认一天失效
        redisCacheConfigurationMap.put(RedisCacheNames.ARTICLE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())));
        return redisCacheConfigurationMap;
    }  
  
  @Bean
  // 通过改造Spring提供的RedisTemplate实现自定义RedisTemplate
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
      // 为了开发方便，一般直接使用<String,Object>
      RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
      template.setConnectionFactory(factory);
      Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
      ObjectMapper om = new ObjectMapper();
      om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
      om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
      jackson2JsonRedisSerializer.setObjectMapper(om);
      StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

      template.setKeySerializer(stringRedisSerializer); // key采用String的序列化方式
      template.setHashKeySerializer(stringRedisSerializer); // hash的key也采用String的序列化方式
      template.setValueSerializer(jackson2JsonRedisSerializer); // value序列化方式采用jackson
      template.setHashValueSerializer(jackson2JsonRedisSerializer); // hash的value序列化方式采用jackson
      template.afterPropertiesSet();

      return template;
  }
  
  @Bean
  public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
      return redisTemplate.opsForHash(); // 简化原生API调用
  }

  @Bean
  public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
      return redisTemplate.opsForValue(); // 简化原生API调用
  }

  @Bean
  public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
      return redisTemplate.opsForList(); // 简化原生API调用
  }

  @Bean
  public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
      return redisTemplate.opsForSet(); // 简化原生API调用
  }

  @Bean
  public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
      return redisTemplate.opsForZSet(); // 简化原生API调用
  }
}
```

#### 12.4.3.2 自定义RedisUtils工具类

在`RedisUtils.java`中封装Redis工具类：

```java
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    /**
     * 设置值与过期时间
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key,Object value, long expire) {
        valueOperations.set(key, JsonUtils.toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置值，默认过期时间1天
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 获取对象，同时设置过期时间
     * @param key
     * @param clazz
     * @param expire
     * @param <T>
     * @return
     */
    public <T> T getObj(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : JsonUtils.toObj(value, clazz);
    }

    /**
     * 获取对象，不设置过期时间
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObj(String key, Class<T> clazz) {
        return getObj(key, clazz, NOT_EXPIRE);
    }

    /**
     * 获取值，同时设置过期时间
     * @param key
     * @param expire
     * @return
     */
    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    /**
     * 获取值，不设置过期时间
     * @param key
     * @return
     */
    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    /**
     * 删除
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 更新过期时间
     * @param key
     */
    public void updateExpire(String key) {
        redisTemplate.expire(key,DEFAULT_EXPIRE,TimeUnit.SECONDS);
    }   
}
```

## 12.5 高级使用

### 12.5.1 Redis.conf

在Windows下的配置文件是redis.windows.conf，Linux下的配置文件是Redis.conf。**值得注意的是**：Windows平台的Redis官方已经放弃维护了（目前是微软在维护），且Redis官方也推荐在Linux环境下使用，在Windows环境下使用Redis可能会出现一些未知的错误。

```yaml
# Redis.conf配置文件详解
# 1. 配置文件相关
### 1.1 配置文件对大小写不敏感
### 1.2 可以使用include引入其他的配置文件，例如：include /path/to/local.conf

# 2. 网络相关
### 2.1 绑定IP：bind 127.0.0.1
### 2.2 一般开启保护模式：protected-mode yes
### 2.3 开启端口：port 6379

# 3. 通用设置
### 3.1 是否以守护线程方式运行，默认未no，需要手动开启未yes：daemonize yes
### 3.2 若以后台方式运行(即以守护线程方式运行)，则需要指定一个pid文件：pidfile /var/run/redis_6379.pid
### 3.3 配置日志级别(debug/verbose/notice/warning)：loglevel notice
### 3.4 配置输出的日志的文件名及位置（若为空，则为标准的输出）：logfile ""
### 3.5 默认的数据库数量为16个：databases 16
### 3.6 是否总是显示Logo（即启动Redis时的彩蛋Banner）：always-show-logo yes

# 4. 快照配置
### 4.1 配置持久化(会持久化到.rdb/.aof中)规则：
		save 900 1  # 若900s内，至少有1个key进行了修改，则会即时进行持久化操作
		save 300 10  # 若300s内，至少有10个key进行了修改，则会即时进行持久化操作
		save 60 10000  # 若60s内，至少有10000个key进行了修改，则会即时进行持久化操作
### 4.2 持久化之后，是否需要Redis继续工作：stop-writes-on-bgsave-error yes
### 4.3 是否压缩rdb文件，需要消耗一些CPU资源：rdbcompression yes
### 4.4 保存rdb文件的时候，是否进行错误的检查校验：rdbchecksum yes
### 4.5 rdb文件保存的目录：dir ./
### 4.6 rdb文件的名称：dbfilename dump.rdb

# 5. 主从复制的配置（在12.5.5详细介绍）

# 6. 安全配置
### 6.1 默认没有密码，但可以设置登录密码（更多地通过命令设置密码:config set requirepass;登录时使用命令: auth 密码）：requirepass 密码

# 7. 客户端限制配置
### 7.1 设置能连接上Redis的最大客户端的数量：maxclients 10000
### 7.2 配置Redis的最大内存容量（默认单位是字节）：maxmemory <bytes>
### 7.3 配置内存到达上限时的处理策略：maxmemory-policy noeviction
##### 7.3.1 volatile-lru策略：只对设置了过期时间的key进行LRU(默认值)
##### 7.3.2 allkeys-lru策略：删除lru算法的key
##### 7.3.3 volatile-random策略：随机删除即将过期的key
##### 7.3.4 allkeys-random策略：随机删除
##### 7.3.5 volatile-ttl策略：删除即将过期的
##### 7.3.6 noeviction策略：永不过期，返回错误

# 8. aof配置
### 8.1 默认不开启aof模式，默认使用rdb方式持久化；在大部分所有的情况下，rdb完全够用：appendonly no
### 8.2 持久化的文件名字：appendfilename "appendonly.aof"
### 8.3 持久化同步机制: 
		appendfsync always  # 每次修改都会同步，消耗性能
		appendfsync everysec  # 每秒执行一次同步，可能会丢失这1s的数据（默认是这种机制）
		appendfsync no  # 不执行同步，这个时候操作系统自己同步数据，速度最快	
### 8.4 配置重写规则
		auto-aof-rewrite-percentage 100  # 
		auto-aof-rewrite-min-size 64mb  # 若aof文件大于64m，会fock一个新的进程来将文件进行重写
```

> 参考博客文章：[Redis的缓存淘汰策略LRU与LFU](https://www.jianshu.com/p/c8aeb3eee6bc)

### 12.5.2 持久化

Redis是内存数据库，如果不将内存中的数据库状态保存到磁盘，那么一旦服务器进程退出，服务器中的数据库状态也会消息，故而使用Redis必须学会持久化（**持久化是面试和工作的重点**）。

#### 12.5.2.1 RDB（Redis DataBase）

**RDB持久化**：在指定的时间间隔内将内存中的数据集快照写入磁盘（即Snapshot快照），恢复时会将快照文件直接读到内存里。Redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件（文件默认名称是`dump.rdb`）。整个过程中，主进程是不进行任何IO操作的，这就确保了极高的性能。**如果需要进行大规模数据的恢复，且对数据恢复的完整性不是很敏感，那么RDB方式要比AOF方式更加的高效。RDB的缺点是最后一次持久化后的数据可能丢失。我们默认的就是RDB，一般情况下不需要修改这个配置**。在生产环境一般会对rdb文件进行备份。在主从复制中，RDB一般用在从机上面进行备用。

**RDB持久化触发机制**：

1. save规则满足的情况下，会自动触发rdb规则，生成rdb文件。
2. 执行FLUSHALL命令，也会触发rdb规则，生成rdb文件。
3. 退出Redis，也会产生rdb文件。

**RDB恢复机制**：

1. 只需要将rdb文件放在Redis的启动目录，那么启动的时候就会自动检查dump.rdb，并恢复其中的数据。
2. 查看rdb文件需要存在的位置：config get dir

**RDB持久化优点**：

1. 适合大规模的数据恢复。
2. 对数据的完整性要求不高。

**RDB持久化的缺点**：

1. 需要一定的时间间隔进程操作。若Redis意外宕机了，这个最后一次修改的数据就没有了。
2. fork进程的时候，会占用一定的内容空间。

#### 12.5.2.2 AOF（Append Only File）

**AOF持久化**： Redis同样会单独创建（fork）一个子进程来进行持久化，将所有的命令都记录下来，恢复的时候将文件（文件默认名称是`appendonly.aof`）全部执行一遍即可。以日志的形式来记录每个写操作，将Redis执行过的所有指令记录下来（**读操作不记录**），只允许追加文件但不可以改写文件。Redis启动之初会读取该文件重新构建数据。AOF持久化默认是不开启的，需要手动进行配置（设置：`appendonly yes`）（详见`12.5.1`）。

**注意**：若`appendonly.aof`有错误，那么Redis是启动不起来的，我们需要使用Redis的`redis-check-aof`工具来修复这个文件（Linux下的修复命令为`redis-check-aof --fix appendonly.aof`）。修复成功后，重启就可以恢复了。

**AOF持久化的优点**：

1. 每一次修改都同步，文件的完整性会更加好。
2. 每秒同步一次，可能会丢失1s的数据。
3. 永不同步，效率最高。

**AOF持久化的缺点**;

1. 相对于数据文件来说，AOF远远大于RDB，修复的速度也比RDB慢。
2. AOF运行效率要比RDB慢，故而Redis默认的配置是RDB持久化。

#### 15.5.2.3 持久化总结



### 12.5.3 发布订阅



### 12.5.4 主从复制



### 12.5.5 哨兵模式



### 12.5.6 缓存穿透与雪崩





## 12.6 本项目中的应用

### 12.6.1 高速缓存





### 12.6.2 统计网站UV/PV





> 参考博客文章：**[Redis命令参考](http://doc.redisfans.com/index.html)**、**[B站狂神说Redis视频](https://www.bilibili.com/video/BV1S54y1R7SB?from=search&seid=5939754712593162694)**、**[Redis深度历险-钱文品]()**、[Redis中文网站](https://www.redis.net.cn/)、[Redis菜鸟教程](https://www.runoob.com/redis/)



ElasticSearch



RabbitMq



# # 个人建站流程

## ## 购买域名

在阿里云购买域名：`progzc.com`，购买期限为1年。

## ## 购买云服务器

在阿里云官网购买云服务器：CPU1核、内存2GiB、操作系统CentOS 8.0 64位、带宽3Mbps。

## ## 进行ICP备案

在阿里云ICP备案系统平台按照步骤备案：基础信息校验-->主办者信息填写-->网站信息填写-->上传资料（**本省注册不需要提交暂住证**）--> 5天作用ICP备案成供

## ## Docker部署项目



















