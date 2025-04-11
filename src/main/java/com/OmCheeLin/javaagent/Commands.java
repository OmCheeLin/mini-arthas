package com.OmCheeLin.javaagent;

import com.OmCheeLin.javaagent.monitor.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.lang.instrument.Instrumentation;

@Command(name = "mini-arthas",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
                MemoryCommand.class,
                ThreadCommand.class,
                ExecutionTimeCommand.class,
                ClassCommand.class,
                ProfilerCommand.class
        })
public class Commands implements Runnable {

    static Instrumentation instrumentation;

    // 在Java Agent premain中初始化
    public static void init(Instrumentation inst) {
        instrumentation = inst;
    }

    @Override
    public void run() {
        // 默认行为：显示帮助信息
        new CommandLine(this).usage(System.out);
    }
}


@Command(name = "memory", description = "内存监控命令")
class MemoryCommand {
    @Command(name = "info", description = "显示内存信息")
    public void memoryInfo() {
        MemoryMonitor.getMemoryInfo();
    }

    @Command(name = "heapdump", description = "生成堆转储文件")
    public void heapDump() {
        MemoryMonitor.heapDump();
        System.out.println("Heap dump created.");
    }
}


@Command(name = "thread", description = "线程监控命令")
class ThreadCommand {
    @Command(name = "list", description = "列出所有线程")
    public void threadList() {
        ThreadMonitor.getThreadInfo();
    }
}


@Command(name = "exetime", description = "方法执行时间监控")
class ExecutionTimeCommand implements Runnable{
    @Parameters(index = "0", description = "要监控的类名")
    private String className;
    @Override
    public void run() {
        ExecutionTimeMonitor.getExecutionTime(Commands.instrumentation, className);
    }
}


@Command(name = "class", description = "类加载监控")
class ClassCommand {
    @Command(name = "loaders", description = "显示所有类加载器")
    public void listClassLoaders() {
        ClassMonitor.getAllClassLoader(Commands.instrumentation);
    }

    @Command(name = "source", description = "反编译类源码")
    public void decompileSource(
            @Parameters(index = "0", description = "要反编译的类名")
            String className
    ) {
        ClassMonitor.getClassSourceCode(Commands.instrumentation, className);
    }
}

@Command(name = "profiler", description = "生成火焰图")
class ProfilerCommand implements Runnable {
    @Parameters(index = "0", description = "事件: cpu, wall等")
    private String event;

    @Parameters(index = "1", description = "采样时间")
    private String time;

    @Override
    public void run() {
        ProfilerMonitor.getFrameHtml(event, time);
    }
}

