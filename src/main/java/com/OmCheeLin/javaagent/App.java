package com.OmCheeLin.javaagent;


import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        // 1. execute jps
        Process jps = Runtime.getRuntime().exec("jps");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jps.getInputStream()));
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        // 2. input java process id
        Scanner sc = new Scanner(System.in);
        System.out.print("Please input process id: ");
        String processId = sc.next();


        // 3. get target JVM and load agent
        VirtualMachine vm = VirtualMachine.attach(processId);
        vm.loadAgent("D:\\java_workspace_IDEA\\mini-arthas\\target\\mini-arthas-1.0-jar-with-dependencies.jar");
    }
}
