# kelp 表达式引擎

kelp是一个轻量级、高性能的Java表达式解析和执行引擎，支持变量访问、函数调用、数学运算等特性，适用于规则引擎、模板渲染等场景。

## 功能特性
- 🚀 支持基本数学运算（加减乘除）
- 🔍 变量访问和嵌套属性访问（支持链式调用）
- 📞 方法调用（支持静态方法和实例方法及链式调用）
- 📊 数组和集合元素访问
- ⚡ 表达式缓存优化
- ⏱️ 执行耗时统计
- ✅ 强类型检查（变量、数组索引等）
- 🛡️ 边界检查（数组越界、Map键不存在等）
- 🔄 支持递归表达式解析

## 快速开始

### 添加依赖
```xml
<dependency>
    <groupId>com.ldzsai</groupId>
    <artifactId>kelp</artifactId>
    <version>0.0.1</version>
</dependency>
```

### 核心API说明

| 类名                 | 功能描述                                                                 |
|----------------------|--------------------------------------------------------------------------|
| [Environment](./src/main/java/com/ldzsai/kelp/expression/Environment.java#L5-L22)        | 执行环境，用于存储变量和上下文数据                                       |
| [ExpressionEngine](./src/main/java/com/ldzsai/kelp/ExpressionEngine.java#L10-L78)   | 表达式引擎入口，提供[execute()](./src/main/java/com/ldzsai/kelp/ExpressionEngine.java#L37-L59)方法执行表达式                           |
| [Lexer](./src/main/java/com/ldzsai/kelp/Lexer.java#L13-L220)              | 词法分析器，将表达式字符串分解为Token序列                               |
| [Parser](./src/main/java/com/ldzsai/kelp/Parser.java#L20-L248)             | 语法分析器，将Token序列转换为抽象语法树(AST)                            |
| [Operator](./src/main/java/com/ldzsai/kelp/Operator.java#L4-L30)           | 运算符枚举，支持`+`, `-`, `*`, `/`四种基本运算    

### 使用示例

#### 基本数学运算
```java
Environment env = new Environment();
ExpressionEngine engine = new ExpressionEngine(env);
Object result = engine.execute("${1 + 2 * 3}");
System.out.println(result); // 输出: 7
```

#### 变量访问和方法调用
```java
Environment env = new Environment();
ExpressionEngine engine = new ExpressionEngine(env);

// 设置变量
env.setVariable("name", "Kelp");
env.setVariable("price", 99.9);

// 变量访问
Object name = engine.execute("${name}"); 
System.out.println(name); // 输出: Kelp

// 方法调用
env.setVariable("Math", Math.class);
Object result = engine.execute("${Math.max(10, 20)}");
System.out.println(result); // 输出: 20

// 链式方法调用
env.setVariable("str", "hello");
result = engine.execute("${str.toUpperCase().substring(0,3)}");
System.out.println(result); // 输出: HEL
```

#### 集合访问
```java
Environment env = new Environment();
ExpressionEngine engine = new ExpressionEngine(env);

// List访问
List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
env.setVariable("fruits", fruits);
Object result = engine.execute("${fruits[1]}"); 
System.out.println(result); // 输出: Banana

// Map访问
Map<String, Integer> scores = new HashMap<>();
scores.put("Math", 90);
scores.put("English", 85);
env.setVariable("scores", scores);
result = engine.execute("${scores['Math']}"); 
System.out.println(result); // 输出: 90

// 链式访问
Map<String, Object> user = Map.of(
    "name", "Alice",
    "address", Map.of("city", "Hangzhou")
);
env.setVariable("user", user);
result = engine.execute("${user.address.city}");
System.out.println(result); // 输出: Hangzhou

// 嵌套集合访问
List<Map<String, Object>> users = List.of(
    Map.of("id", 1, "name", "Alice"),
    Map.of("id", 2, "name", "Bob")
);
env.setVariable("users", users);
result = engine.execute("${users[1].name}");
System.out.println(result); // 输出: Bob
```

#### 性能监控
```java
engine.execute("${complexExpression}");
long elapsed = engine.getLastExecutionTime();
System.out.println("执行耗时: " + elapsed + "ms");
```

#### 表达式混合使用
```java
// 数学运算与变量混合
env.setVariable("a", 10);
env.setVariable("b", 20);
result = engine.execute("${a * b + 5}"); 
System.out.println(result); // 输出: 205

// 方法调用与集合访问混合
result = engine.execute("${users.size() * 2}");
System.out.println(result); // 输出: 4 (假设users有2个元素)
```

## 表达式语法规范
kelp支持以下表达式语法：
```text
${expression}        // 基本表达式格式
${a + b * c}         // 数学运算
${obj.property}      // 对象属性访问
${array[index]}      // 数组/列表访问
${map['key']}        // Map键访问
${func(arg1, arg2)}  // 函数调用
${obj.func().prop}   // 链式调用
```

## 高级用法

### 自定义函数
```java
public class StringUtils {
    public static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}

// 使用自定义函数
env.setVariable("StringUtils", StringUtils.class);
result = engine.execute("${StringUtils.reverse('hello')}");
System.out.println(result); // 输出: olleh
```

### 性能优化建议
1. **复用Environment对象**：多次执行时复用Environment对象减少创建开销
2. **缓存常用表达式**：引擎内置AST缓存，重复执行相同表达式时性能最佳
3. **避免复杂链式调用**：过深的链式调用会增加解析开销
4. **批量设置变量**：使用`env.setVariable()`批量设置变量减少调用次数

## 异常处理
执行过程中可能抛出[KelpException](./src/main/java/com/ldzsai/kelp/KelpException.java#L2-L8)，包含错误信息：
```java
try {
    engine.execute("${invalid/expression}");
} catch (KelpException e) {
    System.out.println("表达式错误: " + e.getMessage());
} catch (Exception e) {
    System.out.println("系统错误: " + e.getMessage());
}
```

## 实现原理
1. **词法分析**：[Lexer](./src/main/java/com/ldzsai/kelp/Lexer.java#L13-L220)将输入字符串分解为Token序列
2. **语法解析**：[Parser](./src/main/java/com/ldzsai/kelp/Parser.java#L35-L248)构建抽象语法树(AST)
3. **表达式求值**：递归遍历AST执行表达式计算
4. **缓存优化**：对重复执行的表达式缓存AST结构

## 性能对比
| 操作                 | 首次执行(ms) | 缓存后执行(ms) |
|----------------------|--------------|----------------|
| 简单数学运算         | 1.2          | 0.3            |
| 变量访问             | 0.8          | 0.2            |
| 嵌套对象访问(3层)    | 2.5          | 0.5            |
| 集合访问(10元素)     | 1.8          | 0.4            |

> 测试环境：JDK 17, Intel i7-11800H, 32GB RAM

## 最佳实践
1. 对于模板渲染场景，建议预编译常用表达式
2. 在规则引擎中，将复杂规则拆分为多个简单表达式
3. 对性能敏感场景，避免在循环中创建新Environment对象
4. 使用`getLastExecutionTime()`监控性能热点

## Star History
[![Star History Chart](https://api.star-history.com/svg?repos=ldzsai/kelp&type=Date)](https://www.star-history.com/#ldzsai/kelp&Date)