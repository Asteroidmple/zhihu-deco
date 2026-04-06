# Impeccable Skill 部署说明

## 已完成
✅ 已下载 Impeccable skill 文件到项目目录：
- `.trae-skills/` - 适用于 Trae 国际版
- `.trae-cn-skills/` - 适用于 Trae 中国版

## 手动安装步骤

### 方案 1：使用 PowerShell（推荐）

#### 1. 关闭 Trae IDE
确保 Trae IDE 已完全关闭。

#### 2. 打开 PowerShell 并执行以下命令

**对于 Trae 国际版用户：**
```powershell
# 创建技能目录（如果不存在）
New-Item -ItemType Directory -Force -Path "$env:USERPROFILE\.trae\skills"

# 复制所有技能文件
Copy-Item -Path ".\D:\Documents\GitHub\zhihu-deco\.trae-skills\*" -Destination "$env:USERPROFILE\.trae\skills" -Recurse -Force
```

**对于 Trae 中国版用户：**
```powershell
# 创建技能目录（如果不存在）
New-Item -ItemType Directory -Force -Path "$env:USERPROFILE\.trae-cn\skills"

# 复制所有技能文件
Copy-Item -Path ".\D:\Documents\GitHub\zhihu-deco\.trae-cn-skills\*" -Destination "$env:USERPROFILE\.trae-cn\skills" -Recurse -Force
```

### 方案 2：使用文件资源管理器

1. 打开文件资源管理器
2. 导航到：`D:\Documents\GitHub\zhihu-deco\`
3. 复制 `.trae-skills` 文件夹（国际版）或 `.trae-cn-skills` 文件夹（中国版）
4. 导航到：
   - 国际版：`C:\Users\Administrator\.trae\skills\`
   - 中国版：`C:\Users\Administrator\.trae-cn\skills\`
5. 粘贴所有文件

### 方案 3：使用命令行（CMD）

**国际版：**
```cmd
xcopy /E /I /Y "D:\Documents\GitHub\zhihu-deco\.trae-skills" "%USERPROFILE%\.trae\skills"
```

**中国版：**
```cmd
xcopy /E /I /Y "D:\Documents\GitHub\zhihu-deco\.trae-cn-skills" "%USERPROFILE%\.trae-cn\skills"
```

## 验证安装

### 3. 重启 Trae IDE
完成复制后，重新启动 Trae IDE。

### 4. 检查技能是否可用
在 Trae 中打开任意项目，尝试使用 Impeccable 命令，例如：
- `/teach-impeccable` - 一次性设置
- `/audit` - 运行质量检查
- `/polish` - 最终优化

## 已安装的命令列表

Impeccable 提供 20 个设计优化命令：

| 命令 | 功能 |
|------|------|
| `/teach-impeccable` | 一次性设置，收集设计上下文 |
| `/audit` | 运行技术质量检查（无障碍、性能、响应式） |
| `/critique` | UX 设计评审 |
| `/normalize` | 对齐设计系统标准 |
| `/polish` | 发布前最终优化 |
| `/distill` | 简化到本质 |
| `/clarify` | 改进不清晰的 UX 文案 |
| `/optimize` | 性能优化 |
| `/harden` | 错误处理、国际化、边界情况 |
| `/animate` | 添加有意义的动效 |
| `/colorize` | 引入策略性色彩 |
| `/bolder` | 放大胆设计 |
| `/quieter` | 收敛过度张扬的设计 |
| `/delight` | 添加愉悦细节 |
| `/extract` | 提取为可复用组件 |
| `/adapt` | 适配不同设备 |
| `/onboard` | 设计 onboarding 流程 |
| `/typeset` | 优化字体、层级、大小 |
| `/arrange` | 优化布局、间距、视觉节奏 |
| `/overdrive` | 添加技术亮点效果 |

## 使用示例

```
# 完整工作流
/audit /normalize /polish blog

# 针对特定场景
/audit dashboard         # 检查仪表板组件
/critique landing page   # 评审落地页 UX
/polish settings page    # 优化设置页面
```

## 注意事项

1. **技能依赖**：Impeccable 基于 Anthropic 的 frontend-design skill 构建，提供更深入的设计指导
2. **反模式**：技能包含明确的反模式指导，避免常见设计错误
3. **参考文件**：包含 7 个领域特定的参考文件（排版、色彩、空间、动效、交互、响应式、文案）

## 参考资料

- 官方网站：https://impeccable.style
- GitHub 仓库：https://github.com/pbakaus/impeccable
