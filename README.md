# Zhihu-deco（知乎·简悦）：注重隐私、互联网个人权利和无广告的知乎客户端

本项目还不够完善，欢迎 PR。

Zhihu-deco 独创本地推荐算法，把内容推荐完全放在本地进行，为您提供和筛选高质量内容。
本地推荐算法完全独立于知乎算法，依赖爬虫运行，可以自由定制各种推荐权重，保证看到自己想看的内容。
我相信，这点绵薄之力可以帮助广大用户从大公司的手中夺回本该属于我们的权利——选择自己的生活，不被算法奴役的权利。

知乎手机客户端，蹲坑神器。去广告，去推广软文，去推销带货，去盐选专栏。

支持手机端/网页端/混合等多种推荐方案。

可以设置屏蔽词、AI 屏蔽回答、屏蔽用户、屏蔽话题等。

## 下载

告别知乎 110MB+ 的客户端，只要不到 4 MB！

[点我下载](https://github.com/Asteroidmple/zhihu-deco/releases)

[下载最新开发版本](https://github.com/Asteroidmple/zhihu-deco/releases/tag/nightly)

## 捐赠

[爱发电支持作者](https://afdian.com/a/zly2006)

## 路线图

### 已经实现的功能

- 登录
  - 支持手机验证码登录
  - 支持通过扫码在电脑端登录
  - 支持手动设置cookie登录
- 首页推荐
  - 支持 Web 端推荐算法
  - 支持安卓端推荐算法
  - 支持切换 **登录状态 / 非登录状态** 下的推荐，防止信息茧房
- 阅读回答
- 阅读文章
- 朗读内容
  - 听文章
  - 听回答
  - 支持 Pico TTS / Sherpa TTS / Google TTS 多引擎自动选择
- 回答页长按保存图片 **无水印**
- 过滤广告、软文和低质量内容
- 浏览器唤起（洛天依主题）
- 历史记录（本地 + 在线）
- 收藏夹（查看收藏夹内容详情）
- 屏蔽词（支持正则表达式）
- NLP屏蔽词（基于LLM embedding和向量相似度匹配，仅 full 版本）
- 屏蔽用户
- 屏蔽话题
- **导出屏蔽词** & **导入屏蔽词**（支持跨设备迁移）
- 评论区（支持子评论展开）
- 通知中心
- 表情包
  - 经典表情`[惊喜]`强势回归！
- 每日热闻（Daily Story）
- 热榜
- 搜索
- 用户主页（查看回答、文章、想法、关注者等）
- 其他
  - 支持 zse96 v2 签名算法（可以调用99%的网页端API）
  - 支持模拟安卓端 API 调用
  - 自动刷新登录凭证
- 其他（非知乎）
  - 提供了二维码扫码结果展示和复制功能，可用于提取网址、Wi-Fi密码等信息

### 尚未实现的功能（帮助欢迎 PR）

#### 📍 本地推荐系统（核心功能，优先级最高）
> 当前状态：框架已搭建，但功能不完整，选择"本地推荐"模式可能无法正常工作

- [ ] **Room 数据库初始化修复**
  - 问题：注解处理器配置可能导致数据库创建失败
  - 错误信息：`does not exist. Is Room annotation processor correctly configured?`
  - 需要：检查 KSP 配置和 Room schema 生成
- [ ] **相似度推荐算法完善**
  - `LocalHomeFeedViewModel` 已存在，但 `initialUrl` 需要正确配置
  - `LocalRecommendationEngine` 推荐逻辑需要测试和调优
- [ ] **用户行为记录与分析**
  - `UserBehaviorAnalyzer` 已实现基础结构
  - 需要与 UI 交互事件集成
- [ ] **爬虫系统完善**
  - `CrawlingExecutor`：需要完善错误处理和重试机制
  - `ZhihuLocalFeedClientImpl`：需要补充更多内容类型的解析
  - `TaskScheduler`：任务调度逻辑需要优化
- [ ] **混合推荐模式（MIXED）**
  - `MixedHomeFeedViewModel` 当前回退到在线推荐
  - 需要实现在线+本地结果的合并与排序

#### 📍 内容过滤增强
- [ ] **首页重复内容自动过滤**
  - 已有展示次数记录机制
  - 需要与推荐系统集成去重逻辑
- [ ] **NLP 短语屏蔽优化**
  - HanLP 关键词提取已实现（仅 full 版本）
  - 句子嵌入模型已集成（仅 full 版本）
  - 可改进：添加短语权重学习和自适应阈值

#### 📍 回答导航器增强
- [ ] **上一回答预览**
  - `QuestionAnswerNavigator` 预取功能待实现
  - 注意：评论中反馈不佳，需要做 fallback 处理
- [ ] **收藏夹导航的下一回答预取**

#### 📍 UI/UX 改进
- [ ] **PeopleScreen 导航到专栏详情** (`PeopleScreen.kt:872`)
  - 需要先实现 `ColumnContentScreen`
- [ ] **Edge-to-Edge 完整适配** (`EdgeToEdgeCompat.kt:12`)
  - 参考：Google Issue Tracker #298296168
- [ ] **ModalBottomSheet 动画配置** (`MyModalBottomSheet.kt:66,144`)
  - 需要从 component tokens 加载 motionScheme
- [ ] **PeopleScreen 模块化拆分**
  - 当前单文件超过 1000 行，包含 12+ 个 ViewModel
  - 建议按功能拆分为独立文件

#### 📍 代码质量改进
- [ ] **清理 DataHolder.kt 死代码**
  - 约 20+ 处被注释掉的字段需要清理或说明
- [ ] **移除重复依赖**
  - `build.gradle.kts` 中 `material:1.13.0` 重复声明
- [ ] **Utils.kt 协程作用域优化**
  - `telemetry()` 函数应避免使用 `GlobalScope`

### 构建变体说明

| 变体 | 大小 | 包名 | 功能 |
|------|------|------|------|
| **lite** | ~4MB | `com.github.zly2006.zhplus.lite` | 基础功能，无 ML/NLP |
| **full** | 较大 | `com.github.zly2006.zhplus` | 完整功能，含 HanLP NLP |

### 遥测

若您同意（可以在设置关闭），本应用会收集一些匿名的数据，用来统计使用量。您可以随时拒绝遥测，这不影响任何功能的使用。我们收集的数据如下：

- 应用启动次数
- 应用启动时间
- 您的IP地址
- 经过SHA256匿名化后的知乎账号ID（如果您登录了的话）

除此之外，我们不会收集任何其他数据，包括但不限于您的浏览记录、推荐算法的输入输出、屏蔽词列表等。

### See Also

如果对其他知乎客户端感兴趣，这些客户端都不需要你root即可使用，也欢迎尝试：

- [Hydrogen](https://github.com/zhihulite/Hydrogen)
- [Zhihu--](https://github.com/huamurui/zhihu-minus-minus) （极早期开发阶段，功能尚有欠缺）

### 开发贡献指南

详见 [CLAUDE.md](./CLAUDE.md)，包含：
- 项目结构和构建说明
- 代码风格约定
- Android 调试标准流程
- Code Review 要求
- 最新代码审查报告
