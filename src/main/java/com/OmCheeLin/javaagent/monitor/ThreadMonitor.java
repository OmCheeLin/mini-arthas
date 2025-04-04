package com.OmCheeLin.javaagent.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * get thread info
 */
public class ThreadMonitor {

    public static void getThreadInfo() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(threadMXBean.isObjectMonitorUsageSupported(),
                threadMXBean.isSynchronizerUsageSupported());

        for (ThreadInfo threadInfo : threadInfos) {
            StringBuilder sb = new StringBuilder();
            sb.append("name: ").append(threadInfo.getThreadName())
                    .append("threadId: ")
                    .append(threadInfo.getThreadId())
                    .append("threadState: ")
                    .append(threadInfo.getThreadState());
            System.out.println(sb);

            StackTraceElement[] stackTrace = threadInfo.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                System.out.println(stackTraceElement);
            }
        }
    }
}
