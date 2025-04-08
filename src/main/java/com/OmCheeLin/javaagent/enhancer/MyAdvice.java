package com.OmCheeLin.javaagent.enhancer;

import net.bytebuddy.asm.Advice;

public class MyAdvice {

    @Advice.OnMethodEnter
    static long enter(@Advice.AllArguments Object[] args) {
        if (args != null) {
            for (Object arg : args) {
                System.out.println("参数: " + arg);
            }
        }

        return System.nanoTime();
    }

    @Advice.OnMethodExit
    static void exit(@Advice.Enter long value) {
        System.out.println("耗时: " + (System.nanoTime() - value) + " 纳秒");
    }
}
