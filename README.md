# kelp 表达式引擎

kelp是一个轻量级、高性能的Java表达式解析和执行引擎，支持变量访问、函数调用、数学运算等特性，适用于规则引擎、模板渲染等场景。

## 功能特性
 - 🚀 支持丰富的数学运算（加减乘除、取模、幂运算、整除）
 - 🔬 支持比较运算（==, !=, >, <, >=, <=）
 - 🔀 支持逻辑运算符（&&, ||, !）
 - ⚙️ 支持位运算符（&, |, ^, ~, <<, >>, >>>）
 - ❓ 支持三元条件运算符（condition ? trueVal : falseVal）
 - 🔍 变量访问和嵌套属性访问（支持链式调用）
 - 📞 方法调用（支持静态方法和实例方法及链式调用）
 - 📊 数组和集合元素访问
 - ⚡ 表达式缓存优化
 - ⏱️ 执行耗时统计
 - ✅ 强类型检查（变量、数组索引等）
 - 🛡️ 边界检查（数组越界、Map键不存在等）
 - 🔄 支持递归表达式解析
 - 🔧 流水线架构，支持中间件扩展
 - 📈 内置指标收集和日志记录
 - 🔒 线程安全，支持并发调用

## 快速开始

### 添加依赖
```xml
<dependency>
    <groupId>com.ldzsai</groupId>
    <artifactId>kelp</artifactId>
    <version>0.0.3</version>
</dependency>
```

### 核心API说明

| 类名                 | 功能描述                                                                 |
|----------------------|--------------------------------------------------------------------------|
| [KelpEngine](./src/main/java/com/ldzsai/kelp/KelpEngine.java)              | 表达式引擎入口，通过构建器模式创建，提供 `execute()` 和 `evaluate()` 方法 |
| [Environment](./src/main/java/com/ldzsai/kelp/expression/Environment.java)  | 执行环境，用于存储变量和上下文数据                                       |
| [Lexer](./src/main/java/com/ldzsai/kelp/Lexer.java)                        | 词法分析器，将表达式字符串分解为Token序列                               |
| [Parser](./src/main/java/com/ldzsai/kelp/Parser.java)                      | 语法分析器，将Token序列转换为抽象语法树(AST)                            |
| [Operator](./src/main/java/com/ldzsai/kelp/Operator.java)                  | 运算符枚举，支持 `+`, `-`, `*`, `/`, `%`, `**`, `//` 等多种运算        |

### 使用示例

#### 基本数学运算
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();
String result = engine.execute("${1 + 2 * 3}", env);
System.out.println(result); // 输出: 4.0
```

#### 高级数学运算
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();

// 幂运算
String result = engine.execute("${2 ** 3}", env);
System.out.println(result); // 输出: 8.0

// 整除
result = engine.execute("${10 // 3}", env);
System.out.println(result); // 输出: 3.0

// 取模
result = engine.execute("${10 % 3}", env);
System.out.println(result); // 输出: 1.0
```

#### 比较运算
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();
env.setVariable("a", 10);
env.setVariable("b", 20);

System.out.println(engine.execute("${a < b}", env));   // 输出: true
System.out.println(engine.execute("${a > b}", env));   // 输出: false
System.out.println(engine.execute("${a == b}", env));  // 输出: false
System.out.println(engine.execute("${a != b}", env));  // 输出: true
```

#### 逻辑运算符
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();
env.setVariable("a", 1);
env.setVariable("b", 0);

System.out.println(engine.execute("${a && a}", env));  // 输出: true
System.out.println(engine.execute("${a && b}", env));  // 输出: false
System.out.println(engine.execute("${a || b}", env));  // 输出: true
System.out.println(engine.execute("${!b}", env));      // 输出: true
```

#### 位运算符
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();
env.setVariable("a", 5);  // 0101
env.setVariable("b", 3);  // 0011

System.out.println(engine.execute("${a & b}", env));  // 输出: 1 (0001)
System.out.println(engine.execute("${a | b}", env));  // 输出: 7 (0111)
System.out.println(engine.execute("${a ^ b}", env));  // 输出: 6 (0110)
System.out.println(engine.execute("${a << b}", env)); // 输出: 40 (101000)
```

#### 三元运算符
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();
env.setVariable("a", 10);
env.setVariable("b", 20);

System.out.println(engine.execute("${a < b ? 'yes' : 'no'}", env)); // 输出: yes
System.out.println(engine.execute("${a > b ? 'yes' : 'no'}", env)); // 输出: no
```

#### 负数支持
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();
env.setVariable("a", 10);

System.out.println(engine.execute("${-a}", env));    // 输出: -10.0
System.out.println(engine.execute("${--a}", env));   // 输出: 10.0
```

#### 变量访问和方法调用
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();

// 设置变量
env.setVariable("name", "Kelp");
env.setVariable("price", 99.9);

// 变量访问
String name = engine.execute("${name}", env);
System.out.println(name); // 输出: Kelp

// 方法调用
env.setVariable("Math", Math.class);
String result = engine.execute("${Math.max(10, 20)}", env);
System.out.println(result); // 输出: 20

// 链式方法调用
env.setVariable("str", "hello");
result = engine.execute("${str.toUpperCase().substring(0,3)}", env);
System.out.println(result); // 输出: HEL
```

#### 集合访问
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();

// List访问
List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
env.setVariable("fruits", fruits);
String result = engine.execute("${fruits[1]}", env);
System.out.println(result); // 输出: Banana

// Map访问
Map<String, Integer> scores = new HashMap<>();
scores.put("Math", 90);
scores.put("English", 85);
env.setVariable("scores", scores);
result = engine.execute("${scores['Math']}", env);
System.out.println(result); // 输出: 90

// 链式访问
Map<String, Object> user = Map.of(
    "name", "Alice",
    "address", Map.of("city", "Hangzhou")
);
env.setVariable("user", user);
result = engine.execute("${user.address.city}", env);
System.out.println(result); // 输出: Hangzhou

// 嵌套集合访问
List<Map<String, Object>> users = List.of(
    Map.of("id", 1, "name", "Alice"),
    Map.of("id", 2, "name", "Bob")
);
env.setVariable("users", users);
result = engine.execute("${users[1].name}", env);
System.out.println(result); // 输出: Bob
```

#### 获取原始求值结果
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();

// evaluate() 返回原始对象，不经过字符串转换
Object raw = engine.evaluate("${1 + 2}", env);
System.out.println(raw);          // 输出: 3.0 (Double 类型)
System.out.println(raw instanceof Number); // 输出: true
```

#### 表达式混合使用
```java
KelpEngine engine = KelpEngine.create();
Environment env = new Environment();

// 数学运算与变量混合
env.setVariable("a", 10);
env.setVariable("b", 20);
String result = engine.execute("${a * b + 5}", env);
System.out.println(result); // 输出: 205.0

// 方法调用与集合访问混合
result = engine.execute("${users.size() * 2}", env);
System.out.println(result); // 输出: 4 (假设users有2个元素)

// 复杂表达式
result = engine.execute("${2 ** 3 + 10 / 2 - 5}", env);  // 8 + 5 - 5 = 8
System.out.println(result); // 输出: 8.0
```

## 表达式语法规范
kelp支持以下表达式语法：
```text
${expression}                 // 基本表达式格式
${a + b * c}                 // 数学运算
${a ** b}                    // 幂运算
${a // b}                    // 整除
${a % b}                     // 取模
${a > b}                     // 比较运算
${a == b}                    // 相等比较
${a && b}                    // 逻辑与
${a || b}                    // 逻辑或
${!a}                        // 逻辑非
${a & b}                     // 位与
${a | b}                     // 位或
${a ^ b}                     // 位异或
${a << b}                    // 左移
${a >> b}                    // 右移
${a >>> b}                   // 无符号右移
${condition ? a : b}         // 三元运算符
${-a}                        // 负数（一元运算符）
${obj.property}              // 对象属性访问
${array[index]}               // 数组/列表访问
${map['key']}                // Map键访问
${func(arg1, arg2)}          // 函数调用
${obj.func().prop}           // 链式调用
```

## 运算符优先级
kelp表达式引擎支持完整的运算符优先级（从高到低）：

| 优先级 | 运算符 | 说明 |
|--------|--------|------|
| 1 | `**` | 幂运算（右结合）|
| 2 | `*`, `/`, `%`, `//` | 乘除模整除 |
| 3 | `+`, `-` | 加减 |
| 4 | `<<`, `>>`, `>>>` | 位移 |
| 5 | `>`, `<`, `>=`, `<=` | 比较 |
| 6 | `==`, `!=` | 相等 |
| 7 | `&` | 位与 |
| 8 | `^` | 位异或 |
| 9 | `|` | 位或 |
| 10 | `&&` | 逻辑与 |
| 11 | `||` | 逻辑或 |
| 12 | `? :` | 三元运算符 |

## 高级用法

### 构建器模式
```java
KelpEngine engine = KelpEngine.builder()
    .maxCacheSize(500)
    .maxExpressionLength(10000)
    .enableLogging(true)
    .enableMetrics(true)
    .build();
```

### 自定义函数
```java
public class StringUtils {
    public static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}

KelpEngine engine = KelpEngine.create();
Environment env = new Environment();
env.setVariable("StringUtils", StringUtils.class);
String result = engine.execute("${StringUtils.reverse('hello')}", env);
System.out.println(result); // 输出: olleh
```

### 性能优化建议
1. **复用 Environment 对象**：多次执行时复用 Environment 对象减少创建开销
2. **缓存常用表达式**：引擎内置 AST 缓存，重复执行相同表达式时性能最佳
3. **避免复杂链式调用**：过深的链式调用会增加解析开销
4. **批量设置变量**：使用 `env.setVariable()` 批量设置变量减少调用次数

## 异常处理
执行过程中可能抛出 [KelpException](./src/main/java/com/ldzsai/kelp/exception/KelpException.java)，包含错误信息：
```java
try {
    engine.execute("${invalid/expression}", env);
} catch (KelpException e) {
    System.out.println("表达式错误: " + e.getMessage());
} catch (Exception e) {
    System.out.println("系统错误: " + e.getMessage());
}
```

## 实现原理
1. **词法分析**：[Lexer](./src/main/java/com/ldzsai/kelp/Lexer.java) 将输入字符串分解为 Token 序列
2. **语法解析**：[Parser](./src/main/java/com/ldzsai/kelp/Parser.java) 构建抽象语法树(AST)
3. **流水线处理**：[ExpressionPipeline](./src/main/java/com/ldzsai/kelp/pipeline/ExpressionPipeline.java) 协调词法分析、语法分析、求值流程
4. **中间件扩展**：支持前置/后置中间件链，可实现输入校验、日志记录、指标收集等功能

## Star History
[![Star History Chart](https://api.star-history.com/svg?repos=ldzsai/kelp&type=Date)](https://www.star-history.com/#ldzsai/kelp&Date)
