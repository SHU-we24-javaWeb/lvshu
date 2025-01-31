# 项目名称：“小旅SHU”旅游攻略分享交流平台

“小旅shu”旅游攻略分享平台是一个致力于为**广大SHU学生在考试周错峰旅游**提供针对性、实用性出游攻略和交流互动社区的在线平台。用户可以在平台上分享自己的旅行经历，发布个人的旅游攻略，获取其他用户的旅行推荐，并与全球旅行者交流心得。平台支持用户注册、发布攻略、评论互动以及关注其他旅行者，旨在为用户提供一个既能记录旅游经历又能获取他人经验的线上社区。 

## 项目的部署和启动请点击然后移步这里[帮助文档](help.md)

### 本项目分为**多个核心模块，概括如下:**   
> - 用户管理模块支持用户注册、登录、信息修改及权限管理;   
> - 攻略管理模块允许用户发布、展示、搜索  和筛选攻略内容，并提供互动功能如点赞、收藏和评论; 
> - 社区互动模块提供用户关注、话题讨论和实时通知等功能，增强平台的社交性;   
> - 后台管理模块包括管理员内容审核、用户管理和平台数据统计，确保平台内容健康和有序发展。   

## 模块设计（详细说明）
### 1. 用户管理模块
#### 功能说明：
* 用户注册：支持邮箱注册，填写基本信息（昵称、密码等）。	
* 用户登录：支持邮箱+密码登录，增加“忘记密码”功能。
* 用户信息管理：允许用户修改头像、个人简介等信息。
* 权限管理：分为普通用户和管理员两种权限，管理员具备内容审核和用户管理权限。

#### 主要页面：  
注册页面、登录页面、用户个人中心页面、管理员后台用户管理页面。

#### 技术实现：  
**后端**：Java (Spring Boot)，负责处理用户认证、权限分配和数据存储。  
**前端**：HTML + CSS 设计页面布局，JavaScript 实现交互效果。  
**数据库**：MySQL，存储用户基本信息（表：users），包括用户ID、昵称、邮箱、权限等字段。  

### 2. 攻略管理模块
#### 功能说明：
* 发布攻略：用户可上传攻略内容，支持添加标题、描述、图片和标签分类。
* 展示攻略：实现攻略列表分页展示，根据发布时间或热度排序。
* 搜索与筛选：根据关键词、标签或目的地筛选攻略。
* 点赞与收藏：用户可对喜欢的攻略点赞、收藏，并在个人中心查看。
* 评论互动：支持对攻略内容发表评论并回复其他用户的评论。

#### 主要页面：
攻略发布页面、攻略详情页面、攻略列表页面。

#### 技术实现：
**后端**：Java (Spring Boot) 提供 RESTful API，处理攻略增删改查请求。  
**前端**：HTML + CSS 实现界面，JS+Ajax 动态加载评论和点赞状态。  
**数据库**：MySQL，创建攻略表（articles），字段包括攻略ID、标题、内容、作者ID、标签等；评论表（comments）存储评论内容及层级关系。  

### 3. 社区互动模块(待定 omg ysy你快来看看这里) 
#### 功能说明：
* 用户关注：允许用户互相关注，构建个人社交网络。
* 话题讨论：设置热门话题标签，用户可参与讨论并查看相关攻略。
* 实时通知：用户收到互动（如评论回复或被关注）时实时通知。

#### 主要页面：
个人主页、热门话题页、通知中心页面。

#### 技术实现：（待定）
**后端**：
* 用户关系模块：基于 MySQL 建立关系表（relations），存储用户间的关注关系。
* 通知模块：基于消息队列（如 RabbitMQ）实现通知异步发送。  

**前端**：
* 使用 WebSocket 实现实时通知更新。
* 热门话题动态加载，JS 结合后端 API 展示内容。

### 4. 后台管理模块（待定）
#### 功能说明：
* 内容审核：管理员可查看、审核用户发布的攻略和评论，删除不良信息。
* 用户管理：查看用户基本信息，对违规用户进行封禁或解封操作。
* 数据统计：展示平台用户量、攻略发布量、访问量等数据统计图表。

#### 主要页面：
审核管理页面、用户管理页面、数据统计页面。

#### 技术实现：（待定）
**后端**：
* 使用 Spring Boot 集成 Shiro 实现管理员权限控制。
* 数据统计基于数据库操作（如查询发布量、访问量）+ ECharts 图表展示。

**前端**：
* 管理员专属后台界面，支持分页展示待审核内容和用户列表。
* 可视化图表生成：集成 ECharts，动态展示数据变化趋势。

>### 总结综合实现技术栈：
>html + css + java / js + mysql
### 模块开发实现分工：（模块分工可能后期动态调整，这里只做简单的规划，不代表后期具体分工实现）
#### 前端界面：张之胤 杨利亚 
#### 逻辑实现：孔馨怡 殷舜尧 冯俊佳

我们将以网页的形式设计简单易用和多样化的界面实现"小旅SHU"，提供一个集旅游信息分享、社交互动和数据分析为一体的综合服务网站。
