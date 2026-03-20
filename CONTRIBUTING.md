# 贡献指南

感谢您对 Kelp 表达式引擎的兴趣！我们欢迎任何形式的贡献，包括但不限于代码改进、文档完善、Bug 修复和新功能开发。

## 行为准则

请阅读并遵守我们的 [行为准则](CODE_OF_CONDUCT.md)，保持社区友好和包容。

## 如何贡献

### 报告 Bug

如果您发现了 Bug，请使用 GitHub 的 [Bug 报告模板](.github/ISSUE_TEMPLATE/bug_report.md) 提交 Issue。报告中请包含：

- 清晰的 Bug 描述
- 详细的复现步骤
- 期望行为和实际行为
- 环境信息（操作系统、Java 版本、Kelp 版本）
- 相关的日志输出

### 提出新功能

如果您有新功能建议，请使用 GitHub 的 [功能请求模板](.github/ISSUE_TEMPLATE/feature_request.md) 提交 Issue。

### 提交代码

#### 开发环境准备

1. Fork 本仓库
2. 克隆您的 Fork：`git clone https://github.com/<your-username>/kelp.git`
3. 创建新分支：`git checkout -b feature/your-feature-name` 或 `git checkout -b fix/bug-description`

#### 开发规范

- **代码风格**：遵循 Google Java Style Guide
- **命名规范**：使用有意义的变量和方法名
- **注释**：为复杂逻辑添加注释，保持代码可读性
- **测试**：新增功能请同时添加测试用例

#### 提交 Pull Request

1. 在提交前，请确保：
   - 所有测试用例通过（`./gradlew test`）
   - 代码符合项目规范
   - 已更新相关文档

2. 提交您的更改：
   ```bash
   git add .
   git commit -m "feat: 添加新功能描述"
   ```

3. 推送到您的 Fork：
   ```bash
   git push origin feature/your-feature-name
   ```

4. 在 GitHub 上创建 Pull Request

#### Pull Request 描述建议

请在 PR 描述中包含：
- 这个 PR 解决的问题或添加的功能
- 更改类型的标记（feat/fix/docs/refactor/test）
- 测试结果的简要说明

### 文档贡献

欢迎改进文档！您可以：
- 修正拼写和语法错误
- 添加使用示例
- 完善 API 文档
- 翻译文档到其他语言

## 项目结构

```
kelp/
├── src/
│   ├── main/java/com/ldzsai/kelp/
│   │   ├── ExpressionEngine.java   # 表达式引擎入口
│   │   ├── Lexer.java              # 词法分析器
│   │   ├── Parser.java              # 语法分析器
│   │   ├── Operator.java            # 运算符枚举
│   │   ├── KelpException.java       # 异常类
│   │   ├── expression/              # 表达式相关类
│   │   └── token/                   # Token 相关类
│   └── test/                        # 测试代码
├── build.gradle                     # 构建配置
└── README.md                        # 项目文档
```

## 构建和测试

```bash
# 构建项目
./gradlew build

# 运行测试
./gradlew test

# 生成 Javadoc
./gradlew javadoc
```

## 许可证

通过贡献代码，您同意您的贡献将遵循 [MIT 许可证](LICENSE)。

## 联系方式

如果您有任何问题，欢迎通过 GitHub Issues 与我们交流。

感谢您的贡献！
