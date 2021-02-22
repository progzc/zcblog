# zcblog博客项目

基于Vue.js和SSM的前后端分离的个人博客。

## 相关数据文档

1. 数据库脚本链接地址：[zcblog.sql](https://github.com/progzc/zcblog/tree/master/db/zcblog.sql)
2. 博客前台模块开发文档说明书链接地址：[zcblog-front2client-docs](https://github.com/progzc/zcblog/blob/master/docs/zcblog-front2client-docs/zcblog-front2client-docs.md)
3. 博客管理系统前台模块开发文档说明书链接地址：[zcblog-front2manage-docs](https://github.com/progzc/zcblog/blob/master/docs/zcblog-front2manage-docs/zcblog-front2manage-docs.md)
4. 博客后台管理系统模块开发文档说明书链接地址：[zcblog-backend-docs](https://github.com/progzc/zcblog/blob/master/docs/zcblog-backend-docs/zcblog-backend-docs.md)

## 博客项目启动步骤

1. 克隆项目到本地：`git clone https://github.com/progzc/zcblog.git`。
2. 启动Redis：`redis-server.exe  redis.windows.conf`，Redis服务端地址：`http://localhost:6379/`。
3. 在`zcblog-front2client`模块下执行命令`npm intsall`，安装博客前台模块相关依赖。
4. 在`zcblog-front2manage`模块下执行命令`npm install`，安装博客管理系统前台相关依赖。
5. 启动博客后台管理系统`zcblog-backend`，主启动类是`BlogRunApplication`，请求地址`http://localhost:8082/`。
6. 在`zcblog-front2client`模块下执行命令`npm run serve`，启动地址`http://localhost:8080/`。
7. 在`zcblog-front2manage`模块下执行命令`npm run serve`，启动地址`http://localhost:8083/`。

## 展示效果

### 博客前台

PC端主页效果：

![image-20210223004033389](README.assets/image-20210223004033389.png)

PC端标签页效果：

![image-20210223004417684](README.assets/image-20210223004417684.png)

![image-20210223004750488](README.assets/image-20210223004750488.png)

PC端时间轴页展示效果：

![image-20210223004505671](README.assets/image-20210223004505671.png)

PC端文章页展示效果：

![image-20210223004959550](README.assets/image-20210223004959550.png)

![image-20210223005803557](README.assets/image-20210223005803557.png)

![image-20210223005935442](README.assets/image-20210223005935442.png)

![image-20210223010017490](README.assets/image-20210223010017490.png)

移动端主页效果：

![image-20210223004138624](README.assets/image-20210223004138624.png)

移动端标签页效果：

![image-20210223005125679](README.assets/image-20210223005125679.png)

移动端时间轴页面效果

![image-20210223005233697](README.assets/image-20210223005233697.png)

![image-20210223005305264](README.assets/image-20210223005305264.png)

移动端文章页展示效果：

![image-20210223005515439](README.assets/image-20210223005515439.png)

![image-20210223010110813](README.assets/image-20210223010110813.png)

![image-20210223010129838](README.assets/image-20210223010129838.png)

### 博客管理系统前台

登录页面：默认最高级别管理员账号为`admin123`、`admin123`

![image-20210223011922452](README.assets/image-20210223011922452.png)

添加文章：

![image-20210223012014592](README.assets/image-20210223012014592.png)

![image-20210223012553578](README.assets/image-20210223012553578.png)

![image-20210223012754564](README.assets/image-20210223012754564.png)

文章列表：

![image-20210223012032794](README.assets/image-20210223012032794.png)

标签管理：

![image-20210223012114188](README.assets/image-20210223012114188.png)

管理员列表：

![image-20210223012229121](README.assets/image-20210223012229121.png)

角色管理：

![image-20210223012257877](README.assets/image-20210223012257877.png)

SQL监控页面：

![image-20210223012327224](README.assets/image-20210223012327224.png)

## 尚未完成功能

- 博客本站搜索
- 相册功能