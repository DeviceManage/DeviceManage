# 哈工大软件工程实践项目-科研设备管理

## 功能需求

开发一个B/S网站，支持科研设备的管理
+ 支持多用户的注册和权限控制，不同单位只能管理各自的设备
+ 设备分类：计算机、家具、仪器…
+ 设备信息维护：名称、编号、图片、型号规格、购置日期….
+ 设备查询
+ 设备信息统计汇总
+ 设备报废管理
+ 设备借出管理

### 第一轮迭代目标

建立基本框架
+ 用户注册登录（暂不考虑单位）`[Done]`
+ 查询设备列表 `[Done]`
+ 显示设备各个属性 `[Done]`
+ 设备信息修改、删除 `[Done]`
+ 设备信息上传 `[Done]`

### 第二轮迭代目标

+ 单位创建、加入、管理 `[Done]` 
+ 权限控制（每个设备归属到具体单位） `[Done]`
+ 设备的添加与修改 `[Done]`
+ 后台人员与组别管理 `Done`
+ 密码重置 `Done`
+ 设备报废管理 `Done`
+ 设备借出管理 `Done`
+ 设备信息统计 `Done`
+ 前端优化 `Done`


## 项目结构

Spring Boot + Thymeleaf + PostgreSQL

### Entity

#### 设备

| 属性     | 说明                              |
|--------|---------------------------------|
| did    | 设备id，int值，从0增长，作为设备实体的统一标识      |
| dname  | 设备名，限长255字节                     |
| dtype  | 设备属性，int值，0代表计算机，1代表家具类，2代表其他仪器 |
| dimage | 设备图片的路径，限长255字节                 |
| buydate | 设备购入日期                          |
| detail | 设备说明（型号规格等）                     |
| dgroup | 设备所属组  -1表示公共设备                 |
| dprivi | 设备权限，0=所有人可访问，3=本组人可访问        |
| dstate | 设备状况，0=正常，1=报废，2=借出|
| tmpgid | 借出组 |

+ 5.29 edit: 加入dgroup 和 dprivi 字段
+ 6.14 edit: 加入dstate字端

#### 用户 siteuser

| 属性      | 说明                                |
|---------|-----------------------------------|
| uid     | 用户id，int值，从0增长，作为用户的统一标识          |
| uname   | 用户名，限长255字节                       |
| upasswd | 用户密码的散列值(SHA-256)                 |
| ugroup | 用户所属组                             
| uprivi | 用户权限，0=有权查看，3=有权编辑，5=组管理员，7=超级管理员 |

#### 组 devicegroup
| 属性      | 说明                       |
|---------|--------------------------|
|gid| 组id |
|gname|组名| 
|gcode|组邀请码|

+ 6.14 edit: 加入组名字段

### Service

#### DeviceRepository

这是Spring Data JPA中的仓库接口，用来跟数据库建立连接和进行查询。数据库连接选项配置在`application.properties`，连接的是放在公网服务器上的postgresql

#### DeviceService

这里是数据操作的核心逻辑。

#### 

### Controller

#### IndexController `/`

根路由

+ `/` 主页
+ `/register` 注册前端
+ `/register/check` 注册接口
+ `/login` 登录前端
+ `/login/check` 登录接口
+ `/reset_sessions` 清理注册登录session
+ `/resetpassword` 重置密码（get，post）

#### ImageController `/getimage/{hash}`

用于返回图片，图片存储在src/main/resourses/static/images/，文件名规则是上传时间戳做sha256取前一半加jpg后缀。

#### DeviceController `/devices`

+ `/{id}` 查询单个设备
+ `/edit/{id}` 用GET请求是展示表单，用POST请求是进行修改的api
+ `/upload` 上传前端页面
+ `/upload/check` 上传接口
+ `/delete/{id}` 删除接口

#### MainController `/main`

+ `/` 查询所有设备，
+ `/join` 加入实验组，get返回静态html，post处理请求
+ `/reset_group` 退出实验组接口
+ `/statistics` 列出统计数据
#### AdminController `/admin`

后台管理路由

+ `/` 后台主页，超级管理员可查看，组管理员访问时跳转到对应group管理页
+ `/createGroup` 组新建接口，指定组名，自动生成邀请码
+ `deleteGroup` 组删除接口，删除时会释放组内成员到无组别
+ `/group/{gid}` 组管理页
+ `/group/{gid}/updateUser` 组内成员权限修改
+ `/group/{gid}/removeUser` 组内踢出成员
+ `/group/{gid}/updateGroupName` 组名修改
+ `/group/{gid}/generateInvite` 邀请码生成接口（不返回字符串，存到数据库里，可以在组管理页面上查看）
+ `/modifyUser` 无组别人员信息修改接口（权限，所属组）
+ `/remoeUser` 无组别人员账号注销接口

#### 注释

+ 权限控制实现：一个未加入实验室的用户只能访问公开设备，一个加入实验室的用户只能访问本实验室下的设备和公开设备
+ 借入借出：直接在编辑页面实现，由设备所有方的用户来执行借出和归还（即将设备状态重新标记为正常）。权限：借入方有权查看详情，无权修改。公开设备不被借入借出（表单可以正常提交，但数据库不会记录借出状态）。
