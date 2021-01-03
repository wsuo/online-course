# online-course
SpringCloud Vue前后端分离-开发企业级在线视频课程系统

## 项目结构
本项目为前后端分离的结构，是逻辑分离，物理上不分离，也就是在一个文件夹下的，在逻辑上分为前端模块和后端模块，另外还有功能模块和文档：

前端模块:
- admin：系统的后台管理页面，用户为 `user` 。
- web：用户的访问页面，用户为 `member` 。
>数据的展示，给用户提供图形化界面，与后端交互。

后端模块:
- system：系统模块，包括用户角色的管理。
- business：业务模块，包括课程、讲师、会员的管理。
- file：文件模块，包括视频图片的文件上传，对接阿里云 `OSS` 接口。
- gateway：网关，项目的入口，将请求转发至各模块，同时负责请求的拦截、权限的控制。
- server：无功能，为其他模块提供基本类，包括实体类和 `Service` 以及工具类。
>数据的获取，提供数据接口供前端调用，与数据库交互。

功能模块:
- generator：继承 `Mybatis` 的代码生成器，以及 `Controller` `Service` `Vue` 的增删改查代码生成。
>生成通用代码，使用 `Freemarker` 模板引擎实现。

文档模块：
- doc：`Sql`、`Json`、`UML` 类图等。
>存放文档，比如数据库初始化文件和测试文件，以及系统资源的配置文件。

物理上该项目是 `Maven` 的父子工程，父工程为 `online_course`，子工程为 `gateway`、`system`、`business`、`file`。

## 项目启动
1. 首先应保证 Nacos 的正常运行，在 `gateway` 模块下的 `yml` 配置文件中配置 `Nacos` 地址；
2. 使用 `doc` 模块下的 `all.sql` 文件初始化数据库；
3. 启动 Redis 客户端，保证 `6379` 端口不被占用。
4. 修改 `API` 的密钥：这里用到了阿里云的 `OSS` 服务，对应的密钥需要自己去官网申请然后配置在 `file` 模块下；

## 重点难点
- 环境的搭建；
- 代码生成器；
- 文件上传和视频播放；
- 通用权限设计；

注意：本项目没有使用诸如 `Spring Security` 等安全框架，而是通过用户角色资源的配置实现了权限管理，如果后面想修改权限管理，需要先登陆控制台，然后在资源配置的选项下，将 `doc` 模块下的 `json` 文件拷贝到文本框中然后保存，退出登陆用户即可重新登录即可更新权限。

## 学习技巧
- 养成记笔记的习惯；
- 养成阶段性提交代码的习惯；