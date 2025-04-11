package com.OmCheeLin.javaagent.monitor;

import com.OmCheeLin.javaagent.one.profiler.AsyncProfiler;

import java.io.IOException;

import static com.OmCheeLin.javaagent.Constants.PROFILER_PATH;


public class ProfilerMonitor {
    public static void getFrameHtml(String event, String time) {
        try {
            // 初始化 profiler（.so文件所在目录）
            AsyncProfiler profiler = AsyncProfiler.getInstance(PROFILER_PATH);
            // 生成火焰图
            String cmd = String.format("start,event=%s,interval=10ms", event);
            profiler.execute(cmd);
            Thread.sleep(Integer.parseInt(time) * 1000L); // 采样时长
            profiler.execute("stop,file=flamegraph.html"); // 生成带样式的火焰图
            System.out.println("火焰图已生成。");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
