package com.OmCheeLin.javaagent.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.List;

public class MemoryMonitor {

    // print memory info
    public static void getMemoryInfo() {
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        // MemoryType: HEAP, NON_HEAP
        System.out.println("HEAP: ");
        doGetMemoryInfo(memoryPoolMXBeans, MemoryType.HEAP);
        System.out.println("NON_HEAP: ");
        doGetMemoryInfo(memoryPoolMXBeans, MemoryType.NON_HEAP);
    }

    private static void doGetMemoryInfo(List<MemoryPoolMXBean> beans, MemoryType type) {
        // ANSI color
        final String YELLOW = "\u001B[33m";
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";

        System.out.printf("%s%-25s %12s %12s %12s%s\n",
                GREEN, "Memory Pool", "Used(M)", "Committed(M)", "Max(M)", RESET);

        beans.stream()
                .filter(x -> x.getType().equals(type))
                .forEach(x -> {
                    String name = x.getName();
                    long used = x.getUsage().getUsed() / (1024 * 1024);
                    long committed = x.getUsage().getCommitted() / (1024 * 1024);
                    long max = x.getUsage().getMax();

                    // Format the numeric display
                    String maxDisplay = (max == -1) ? "N/A"
                            : String.format("%d", max / (1024 * 1024));

                    System.out.printf("%s%-25s %12d %12d %12s%s\n",
                            YELLOW,
                            name,
                            used,
                            committed,
                            maxDisplay,
                            RESET);
                });

        System.out.println("-----------------------------------------------------------");
    }
}
