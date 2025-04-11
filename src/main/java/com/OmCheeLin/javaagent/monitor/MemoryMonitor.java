package com.OmCheeLin.javaagent.monitor;

import com.sun.management.HotSpotDiagnosticMXBean;
import picocli.CommandLine;

import java.io.IOException;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MemoryMonitor {
    // print memory info
    public static void getMemoryInfo() {
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        // MemoryType: HEAP
        System.out.println("HEAP: ");
        doGetMemoryInfo(memoryPoolMXBeans, MemoryType.HEAP);
        // MemoryType: NON_HEAP
        System.out.println("NON_HEAP: ");
        doGetMemoryInfo(memoryPoolMXBeans, MemoryType.NON_HEAP);
        // NIO memory
        System.out.println("NIO Memory: ");
        doGetNioMemory();
    }

    public static void heapDump() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        HotSpotDiagnosticMXBean hotSpotDiagnosticMXBean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
        try {
            hotSpotDiagnosticMXBean.dumpHeap(simpleDateFormat.format(new Date()) + ".hprof", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void doGetNioMemory() {
        final String YELLOW = "\u001B[33m";
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";

        List<BufferPoolMXBean> bufferPoolMXBeans = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);

        System.out.printf("%s%-35s %12s %12s%s\n",
                GREEN, "Buffer Pool", "Used(M)", "Capacity(M)", RESET);

        for (BufferPoolMXBean bufferPoolMXBean : bufferPoolMXBeans) {
            String name = bufferPoolMXBean.getName();
            long used = bufferPoolMXBean.getMemoryUsed() / (1024 * 1024);
            long capacity = bufferPoolMXBean.getTotalCapacity() / (1024 * 1024);

            System.out.printf("%s%-35s %12d %12d%s\n",
                    YELLOW,
                    name,
                    used,
                    capacity,
                    RESET);
        }

        System.out.println("-----------------------------------------------------------");
    }

    private static void doGetMemoryInfo(List<MemoryPoolMXBean> beans, MemoryType type) {
        // ANSI color
        final String YELLOW = "\u001B[33m";
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";

        System.out.printf("%s%-35s %12s %12s %12s%s\n",
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

                    System.out.printf("%s%-35s %12d %12d %12s%s\n",
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
