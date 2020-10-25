# 0 技术方案

`Springboot`+`Spring` + `SpringMVC` + `MyBatis` + `MyBatisPlus`：主流web框架；

`Shiro`：认证与鉴权（一大部分工作量在这里）；

`Redis`：缓存（例如：jedis/Lettuce....，本项目采用Lettuce作为Redis的后端实现）；

`RabbitMq`：消息中间件；

`ElasticSearch`：搜索模块；

`quartz`：定时器，定时发送文章（**此功能不一定实现**），定时清除OSS上的无效图片，将云存储上逻辑删除的资源间隔指定时间彻底清除，相当于完成回收站的类似功能（**此功能不一定实现**）；

`MySql` + `Druid`：数据库 + 连接池；

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

主流的权限管理表应该至少需要10张表，包括`权限表`、`角色表`、`组表`、`用户表`4张主表；`用户权限表`、`用户角色表`、`用户组表`、`角色权限表`、`组角色表`、`组权限表`6张中间表。本项目相当于做了一定的简化。关于"用户·角色·权限·表"的设计可以参见相关博客文章：**[用户·角色·权限·表的设计](https://blog.csdn.net/weixin_42476601/article/details/82346740)**

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

- zcblog-backend为父模块，父模块有两个作用：引入SpringBoot启动依赖；管理jar包，统一所有子模块依赖项的版本。

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
    # 为资源文件建立默认映射：若设置为false，会导致不能访问Swagger
    add-mappings: true
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
6. 本项目不提供API连接，使用`Swagger`来实现前后端人员API设计的及时沟通。

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































# # 个人建站流程

## ## 购买域名

在阿里云购买域名：`progzc.com`，购买期限为1年。

## ## 购买云服务器

在阿里云官网购买云服务器：CPU1核、内存2GiB、操作系统CentOS 8.0 64位、带宽3Mbps。

## ## 进行ICP备案

在阿里云ICP备案系统平台按照步骤备案：基础信息校验-->主办者信息填写-->网站信息填写-->上传资料（**本省注册不需要提交暂住证**）。





















