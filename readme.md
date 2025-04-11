# Mini-Arthas - 轻量级 JVM 诊断工具

https://img.shields.io/badge/Java-11%2B-orange  
https://img.shields.io/badge/License-MIT-blue  
https://img.shields.io/badge/Purpose-Education%20%26%20Research-green

​**​项目作者：OmCheeLin​**​  
**前言​**​：如果你正在学习 arthas，或者是 java-agent 等 JVM 监控技术，那么本项目是入门的一个很好的选择。

---

## 🌟 项目简介

一个基于 Java Agent 技术的轻量级 JVM 诊断工具，集成以下核心功能：

- ​**​内存相关**：堆/非堆内存监控 + 一键堆转储
- ​**​线程相关​**​：线程状态跟踪 + 堆栈分析
- ​**​类相关​**​：类加载器打印 + 字节码反编译
- ​**​接口耗时统计**：ByteBuddy 动态字节码增强实现方法耗时统计
- ​**​​**​**async-profiler**：集成 async-profiler 生成火焰图

---

## ✨ 功能特性

| 模块     | 命令示例                          | 功能描述                    |
| ------ | ----------------------------- | ----------------------- |
| 内存监控   | `memory info`                 | 可视化堆/非堆/NIO Buffer 内存分布 |
|        | `memory heapdump`             | 生成时间戳命名的堆转储文件(.hprof)   |
| 线程分析   | `thread list`                 | 列出所有线程状态及完整堆栈跟踪         |
| 方法耗时统计 | `exetime com.example.MyClass` | 动态增强指定类的方法执行时间监控        |
| 类加载分析  | `class loaders`               | 显示所有类加载器                |
|        | `class source MyClass`        | 反编译指定类字节码               |
| 性能剖析   | `profiler cpu 30`             | 生成 30 秒 CPU 热点火焰图(HTML) |

---

## ⚡ 快速开始

### 环境要求

- JDK 17
- Linux (async_profiler 只支持 Linux 和 macOS)

### 四步启动

```bash
# 1. 克隆项目
git clone https://github.com/OmCheeLin/mini-arthas.git

# 2. 修改配置
修改 com.OmCheeLin.javaagent.Constants 相关路径

# 3. 编译打包
mvn clean compile assembly:single
mvn package

# 4. 运行测试
java -jar boot-demo-0.0.1.jar
java -jar OmCheeLin-agent.jar

```

附：修改配置

![](C:\Users\17298\AppData\Roaming\marktext\images\2025-04-11-16-48-07-image.png)

---

## 🛠️ 使用指南

### 内存监控

```bash
# 显示内存使用详情（MB单位）
memory info

# 生成堆转储文件
memory heapdump
```

### 线程诊断

```bash
# 显示所有线程状态
thread list
```

### 方法级监控

```bash
# 监控指定类所有方法
exetime com.example.MyController
```

---

## 🧠 实现原理

### 技术架构

### 核心机制

1. ​**​Java Agent 动态加载​**​
   
   - 通过 Attach API 实现运行时注入
   - Instrumentation API 修改类定义

2. ​**​字节码增强技术​**​
   
   java
   
   复制
   
   ```java
   // ByteBuddy 增强示例
   new AgentBuilder.Default()
    .type(ElementMatchers.named(className))
    .transform((builder, ...) -> 
        builder.visit(Advice.to(MyAdvice.class))
    );
   ```

3. ​**​JMX 监控体系​**​
   
   - 通过 `ManagementFactory` 获取 MXBean
   - 支持 MemoryPool/Thread/BufferPool 等 20+ 指标

---

## 📂 项目结构

```
com/OmCheeLin/javaagent
├── App.java               # 项目启动入口
├── Agent.java             # Java Agent 入口 
├── Commands.java          # picocli框架(类似 JCommand)
├── monitor/               # 监控实现
│   ├── MemoryMonitor.java # 内存监控
│   ├── ThreadMonitor.java # 线程栈监控
│   └── ......
├── one.profiler           # async-profiler相关
└── enhancer/              # 字节码增强
    └── MyAdvice.java      # 方法耗时统计切面(ByteBuddy)
```

---

## ❓ 常见问题

​**​Q1: 无法附加到目标JVM​**​  
👉 检查Constants中路径是否正确，最好写绝对路径。

​**​Q2: 火焰图生成失败​**​  
👉 确保已放置 async-profiler 的 libasyncProfiler.so

---

## 🤝 如何贡献

如果你对本项目感兴趣，欢迎 fork仓库并提交 PR。

---

## 📜 许可协议

MIT License © 2025 OmCheeLin

---


