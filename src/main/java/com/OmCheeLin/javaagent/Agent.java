package com.OmCheeLin.javaagent;

import com.OmCheeLin.javaagent.monitor.ClassMonitor;
import com.OmCheeLin.javaagent.monitor.MemoryMonitor;
import com.OmCheeLin.javaagent.monitor.ThreadMonitor;

import java.lang.instrument.Instrumentation;

import static com.OmCheeLin.javaagent.monitor.MemoryMonitor.heapDump;


public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        MemoryMonitor.getMemoryInfo();
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        // MemoryMonitor.getMemoryInfo();
        // MemoryMonitor.heapDump();
        // ThreadMonitor.getThreadInfo();
        // ClassMonitor.getAllClassLoader(inst);
        ClassMonitor.getClassSourceCode(inst);
    }
}
