package com.OmCheeLin.javaagent.monitor;

import com.OmCheeLin.javaagent.enhancer.MyAdvice;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.Scanner;

public class ExecutionTimeMonitor {
    public static void getExecutionTime(Instrumentation inst) {
        Scanner sc = new Scanner(System.in);
        System.out.print("input class name: ");
        String className = sc.next();
        // use bytebuddy
        new AgentBuilder.Default()
                // class name can not be changed
                .disableClassFormatChanges()
                // use retransform to enchance
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                // print error log
                .with(new AgentBuilder.Listener.WithTransformationsOnly(AgentBuilder.Listener.StreamWriting.toSystemOut()))
                // match controller
                .type(ElementMatchers.named(className))
                // use MyAdvice enhance
                .transform(
                        (builder, typeDescription, classLoader, javaModule, protectionDomain) ->
                                builder.visit(Advice.to(MyAdvice.class).on(ElementMatchers.any()))
                )
                .installOn(inst);
    }
}
