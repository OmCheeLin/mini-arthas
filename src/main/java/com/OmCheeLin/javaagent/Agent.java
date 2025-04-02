package com.OmCheeLin.javaagent;

import com.OmCheeLin.javaagent.monitor.MemoryMonitor;

import java.lang.instrument.Instrumentation;


public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        MemoryMonitor.getMemoryInfo();
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        MemoryMonitor.getMemoryInfo();
    }
}
