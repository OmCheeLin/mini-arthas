package com.OmCheeLin.javaagent;

import com.OmCheeLin.javaagent.monitor.ClassMonitor;
import com.OmCheeLin.javaagent.monitor.ExecutionTimeMonitor;
import com.OmCheeLin.javaagent.monitor.MemoryMonitor;
import com.OmCheeLin.javaagent.monitor.ThreadMonitor;

import java.lang.instrument.Instrumentation;
import java.util.Scanner;

import static com.OmCheeLin.javaagent.monitor.MemoryMonitor.heapDump;


public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        MemoryMonitor.getMemoryInfo();
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("菜单: \n")
                    .append("1: 查看内存使用情况\n")
                    .append("2: 生成堆内存快照\n")
                    .append("3: 打印线程栈信息\n")
                    .append("4: 打印所有类加载器\n")
                    .append("5: 打印类的源码\n")
                    .append("6: 打印controller接口参数和耗时\n")
                    .append("7: 退出");
            System.out.println(sb);
            Scanner sc = new Scanner(System.in);
            System.out.print("input your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: MemoryMonitor.getMemoryInfo(); break;
                case 2: MemoryMonitor.heapDump(); break;
                case 3: ThreadMonitor.getThreadInfo(); break;
                case 4: ClassMonitor.getAllClassLoader(inst); break;
                case 5: ClassMonitor.getClassSourceCode(inst); break;
                case 6: ExecutionTimeMonitor.getExecutionTime(inst); break;
                case 7: return;
            }
        }
    }
}
