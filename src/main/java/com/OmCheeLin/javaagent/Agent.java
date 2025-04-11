package com.OmCheeLin.javaagent;

import com.OmCheeLin.javaagent.monitor.MemoryMonitor;
import picocli.CommandLine;

import java.lang.instrument.Instrumentation;
import java.util.Scanner;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        MemoryMonitor.getMemoryInfo();
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        Commands.init(inst);
        Scanner scanner = new Scanner(System.in);  // 监听用户输入

        System.out.println("Mini-Arthas 已启动，输入命令（输入 'exit' 退出）");


        try {
            while (true) {
                System.out.print(">>> ");
                String input = scanner.nextLine().trim();

                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Mini-Arthas 已退出");
                    break;
                }

                if (!input.isEmpty()) {
                    // 交给 picocli 处理命令
                    new CommandLine(new Commands()).execute(input.split(" "));
                }
            }
        } catch (Exception e) {
            System.out.println("Mini-Arthas 已退出");
        } finally {
            scanner.close();
        }
    }
}
