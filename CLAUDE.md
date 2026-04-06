# Zhihu-deco Agent Instructions

本项目是隐私增强的知乎 Android 客户端，支持本地推荐算法、广告屏蔽、内容过滤。

## 构建与测试

```bash
# 验证修改（必须按顺序执行）
.\gradle-build.ps1 assembleLiteDebug  # 构建 lite 变体
.\gradle-build.ps1 ktlintFormat        # 格式化代码
```

**重要**: 修改后必须先构建验证，再格式化，最后提交。

## 项目结构

- **app**: 主应用（Jetpack Compose UI）
    - `src/main`: 共享代码
    - `src/full`: Full variant（含 NLP）
    - `src/lite`: Lite variant（轻量级）
- **Module**: `sentence_embeddings`（Rust tokenizer，仅 full variant）

### Build Variants
- **lite**: 轻量版 (~4MB)，无 ML 功能，包名 `com.github.zly2006.zhplus.lite`
- **full**: 完整版，含 HanLP NLP，包名 `com.github.zly2006.zhplus`

## 关键约定

### 数据序列化
- **DataHolder** 和 data classes 使用 `camelCase`
- **知乎 API** 返回 `snake_case`
- **自动转换**: `AccountData.fetch*()` 和 `decodeJson()` 内部自动调用 `snake_case2camelCase()`
- 不要手动转换或在 data class 中使用 snake_case

### 重要：避免重复的 Person 类
项目中有两个 `Person` 类：
- `com.github.zly2006.zhihu.Person`（`NavDestination.kt`）：导航用，只有 `id`、`urlToken`、`name`
- `com.github.zly2006.zhihu.data.Person`（`Feed.kt`）：数据模型，包含完整用户信息

导入时使用别名区分：
```kotlin
import com.github.zly2006.zhihu.Person        // 导航用
import com.github.zly2006.zhihu.data.Person as DataPerson  // 数据模型
```

### HTTP 客户端
- 使用 `AccountData.httpClient(context)` 获取配置好的客户端
- Web API 需要 `signFetchRequest(context)` 用于 zse96 v2 签名
- Android API 使用 `AccountData.ANDROID_HEADERS` 和 `ANDROID_USER_AGENT`

### Compose
- Material 3 组件
- 用 `LaunchedEffect` 处理副作用，设置正确的 key
- 用 `collectAsState()` 观察 Flow/StateFlow

### 导航
- 使用 Jetpack Navigation Compose
- 定义 sealed interface `NavDestination` 表示不同页面，包含 route 和参数
- 在编写导航代码前必须检查 NavDestination.kt

## 🚨 应用启动与验证（每次修改后必须执行）

**构建验证是强制要求，不是可选项。每次代码修改后必须按顺序执行以下步骤：**

```bash
# 1. 检查包名（必须先做）
Select-String "applicationId" app/build.gradle.kts
# lite variant: com.github.zly2006.zhplus.lite

# 2. 构建并安装
.\gradle-build.ps1 assembleLiteDebug
adb install -r app/build/outputs/apk/lite/debug/app-lite-debug.apk

# 3. 启动
adb shell am force-stop com.github.zly2006.zhplus.lite
adb shell monkey -p com.github.zly2006.zhplus.lite -c android.intent.category.LAUNCHER 1

# 4. 等待加载（关键！）
sleep 8

# 5. 验证应用状态
adb logcat | Select-String -Pattern "error","Error","ERROR" -Context 0,2 | Select-Object -First 20
```

修改 UI 代码后**必须**：
1. ✅ 构建 + 格式化
2. ✅ 安装到设备
3. ✅ 正确启动应用（检查包名！）
4. ✅ 等待加载完成（至少 8-10 秒）
5. ✅ 使用 ui-test 技能查看当前页面状态：`python3 .github/skills/ui-test/llm_test_helper.py dump`
6. ✅ 先 `dump` 再 `tap`，优先通过 `--tag/--text/--desc` 交互，不使用硬编码坐标 tap
7. ✅ 若目标是无标识可点击节点，使用 `--text "" --index N`（N 来自当前页面 dump）
8. ✅ 交互后再次 `dump` 或截图验证状态
9. ✅ 仅在无 tag/文字可用且必须手势操作时，才使用 `adb shell input swipe` 等手势
10. ❌ 异常时检查 logcat：`adb logcat | Select-String "error"`

---

## 代码风格

# 4. 等待加载（关键！）
sleep 8

# 5. 验证应用状态
adb logcat | Select-String -Pattern "error","Error","ERROR" -Context 0,2 | Select-Object -First 20
```

### UI 调试强制清单
修改 UI 代码后**必须**：
1. ✅ 构建 + 格式化
2. ✅ 安装到设备
3. ✅ 正确启动应用（检查包名！）
4. ✅ 等待加载完成（至少 8-10 秒）
5. ✅ 使用 ui-test 技能查看当前页面状态：`python3 .github/skills/ui-test/llm_test_helper.py dump`
6. ✅ 先 `dump` 再 `tap`，优先通过 `--tag/--text/--desc` 交互，不使用硬编码坐标 tap
7. ✅ 若目标是无标识可点击节点，使用 `--text "" --index N`（N 来自当前页面 dump）
8. ✅ 交互后再次 `dump` 或截图验证状态
9. ✅ 仅在无 tag/文字可用且必须手势操作时，才使用 `adb shell input swipe` 等手势
10. ❌ 异常时检查 logcat：`adb logcat | Select-String "error"`

### P0 - 阻塞性问题（需立即修复）

#### 1. 重复依赖声明
- **位置**: [build.gradle.kts:173](app/src/main/java/com/github/zly2006/zhihu/../build.gradle.kts#L173) 和 [build.gradle.kts:183](app/src/main/java/com/github/zly2006/zhihu/../build.gradle.kts#L183)
- **问题**: `com.google.android.material:material:1.13.0` 被重复声明两次
- **影响**: 增加构建时间，可能导致依赖冲突
- **修复**: 删除其中一行重复声明

#### 2. 残留的 local_changes.patch 文件
- **位置**: 项目根目录 `local_changes.patch`
- **问题**: 该文件包含删除整个 `build.gradle.kts` 的 diff，似乎是误操作或测试遗留
- **影响**: 可能导致混淆，应清理或移至适当位置
- **修复**: 删除此文件或移至 `.git/` 目录

### P1 - 主要问题（发布前需修复）

#### 3. DataHolder.kt 大量注释掉的死代码
- **位置**: [DataHolder.kt](app/src/main/java/com/github/zly2006/zhihu/data/DataHolder.kt)
- **问题**: 多个 data class 中有大量被注释掉的字段（约20+处），如 `Question`、`Comment`、`Relationship` 等
- **影响**: 代码可读性差，增加维护成本
- **建议**: 清理无用的注释代码，或添加说明为何保留

#### 4. Utils.kt 使用 GlobalScope
- **位置**: [Utils.kt:68](app/src/main/java/com/github/zly2006/zhihu/util/Utils.kt#L68)
- **问题**: `telemetry()` 函数使用 `GlobalScope.launch` 启动协程
- **影响**: 不遵循结构化并发原则，可能导致内存泄漏和难以追踪的异常
- **建议**: 改为接受 `CoroutineScope` 参数或使用自定义 Scope

#### 5. 本地推荐系统未完成
- **位置**: `viewmodel/local/` 目录下多个文件
- **问题**:
  - `LocalHomeFeedViewModel` 存在但功能不完整
  - Room 数据库初始化可能失败
  - `CrawlingExecutor` 和相关爬虫系统待完善
  - `MixedHomeFeedViewModel` 回退到在线推荐
- **影响**: 用户选择"本地推荐"模式时可能无法正常工作
- **状态**: 需要系统性完善或明确标记为实验性功能

### P2 - 次要问题（建议修复）

#### 6. TODO 标记待处理
| 文件 | 行号 | 描述 |
|------|------|------|
| [PeopleScreen.kt](app/src/main/java/com/github/zly2006/zhihu/ui/PeopleScreen.kt) | 872 | 导航到专栏详情 |
| [EdgeToEdgeCompat.kt](app/src/main/java/com/github/zly2006/zhihu/util/EdgeToEdgeCompat.kt) | 12 | Edge-to-Edge 适配 |
| [MyModalBottomSheet.kt](app/src/main/java/com/github/zly2006/zhihu/ui/components/MyModalBottomSheet.kt) | 66, 144 | Motion scheme tokens |

#### 7. @Suppress("unused") 注解过多
- **位置**: 多个文件
- **文件列表**:
  - [Feed.kt:1](app/src/main/java/com/github/zly2006/zhihu/data/Feed.kt#L1) - `@file:Suppress("unused")`
  - [WebviewComp.kt:1](app/src/main/java/com/github/zly2006/zhihu/ui/components/WebviewComp.kt#L1) - `@file:Suppress("unused")`
  - [DataHolder.kt:456,466,477](app/src/main/java/com/github/zly2006/zhihu/data/DataHolder.kt#L456-L477) - 多个 Thumbnail 类
  - [MainActivity.kt:82,565](app/src/main/java/com/github/zly2006/zhihu/MainActivity.kt#L82) - TtsState 枚举
- **建议**: 确认这些抑制是否必要，考虑重构以消除警告根源

#### 8. PeopleScreen.kt 过于庞大
- **位置**: [PeopleScreen.kt](app/src/main/java/com/github/zly2006/zhihu/ui/PeopleScreen.kt)
- **问题**: 单文件包含 12+ 个 ViewModel 类和大量私有 Composable 函数，估计超过 1000 行
- **影响**: 维护困难，违反单一职责原则
- **建议**: 将 ViewModel 拆分到独立文件，Composable 函数按功能模块化

#### 9. BlockedKeywordRepository 冗余方法
- **位置**: [BlockedKeywordRepository.kt](app/src/main/java/com/github/zly2006/zhihu/nlp/BlockedKeywordRepository.kt)
- **问题**: `addKeyword()`, `addExactMatchKeyword()`, `addNLPPhrase()` 三个方法逻辑高度相似
- **影响**: 代码冗余，维护时需要同步修改多处
- **建议**: 统一为一个带参数的方法

### P3 - 优化建议（有时间再处理）

#### 10. NLPService.full vs NLPService.lite 差异
- **观察**: Lite 版本正确地提供了空实现 stub，这是好的实践
- **建议**: 保持现有模式，但可考虑使用 expect/actual 机制进一步简化

#### 11. Feed.kt Person.gender 默认值
- **位置**: [Feed.kt:438](app/src/main/java/com/github/zly2006/zhihu/data/Feed.kt#L438)
- **问题**: `gender: Int = 0` 有内联 todo 注释 `// todo: 0做默认合适吗？`
- **建议**: 确认语义，考虑使用枚举或更明确的默认值

#### 12. MainActivity.kt TTS 初始化复杂度高
- **位置**: [MainActivity.kt:256-306](app/src/main/java/com/github/zly2006/zhihu/MainActivity.kt#L256-L306)
- **问题**: TTS 引擎选择逻辑嵌套较深，回调地狱
- **建议**: 考虑使用状态机模式或提取为独立类

### ✅ 正面发现

1. **Full/Lite 变体分离良好**: NLP 相关的 stub 实现清晰
2. **数据序列化约定一致**: camelCase/snake_case 转换集中处理
3. **错误处理规范**: 大量使用 `runCatching` 进行安全错误处理
4. **导航系统设计合理**: NavDestination sealed interface 类型安全
5. **内容过滤架构完整**: Room 数据库 + NLP 语义匹配双层过滤

## 已知问题和待完善功能

### 本地推荐系统
- ~~`LocalHomeFeedViewModel.initialUrl` 抛出错误~~ ✅ 已修复：改为返回 `"local://recommendation"`
- Room 数据库初始化可能失败（注解处理器配置问题）
- `CrawlingExecutor` 和 `ZhihuLocalFeedClientImpl` 爬虫系统待完善
- 混合推荐模式（MIXED）暂时回退到在线推荐

### 内容过滤
- ~~`BaseFeedViewModel.kt:154` 中 `QuestionFeedCard` 渲染为 `TODO()`~~ ✅ 已修复：改为递归调用 `createDisplayItem(context, feed.target)`
- 首页重复内容过滤功能已记录展示次数，但尚未与推荐系统集成

### UI 待办
- ~~`PeopleScreen.kt:793` 导航到收藏夹详情~~ ✅ 已修复：添加导航到 `CollectionContent(collection.id)`
- `PeopleScreen.kt:877` 导航到专栏详情（TODO - 需要先实现 ColumnContentScreen）
- `EdgeToEdgeCompat.kt:12` Edge-to-Edge 适配（TODO）
- `MyModalBottomSheet.kt` ModalBottomSheet 动画配置（TODO）
