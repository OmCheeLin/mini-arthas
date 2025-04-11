package com.OmCheeLin.javaagent.monitor;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.state.ClassFileSourceImpl;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.stream.Collectors;


public class ClassMonitor {

    // print all classloader
    public static void getAllClassLoader(Instrumentation inst) {
        Set<ClassLoader> classLoaders = new HashSet<>();
        // get all class
        Class[] classes = inst.getAllLoadedClasses();
        for (Class clazz : classes) {
            classLoaders.add(clazz.getClassLoader());
        }
        String str = classLoaders.stream().map(x -> {
            if (x == null) {
                return "BootStrapClassLoader";
            } else {
                return x.toString().split("@")[0];
            }
        }).distinct().sorted(String::compareTo).collect(Collectors.joining("\n"));
        System.out.println(str);
    }


    // print the source code of class
    public static void getClassSourceCode(Instrumentation inst, String className) {
        Class[] classes = inst.getAllLoadedClasses();
        for (Class clazz : classes) {
            if (clazz.getName().equals(className)) {
                ClassFileTransformer transformer = new ClassFileTransformer() {
                    @Override
                    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                        System.out.println("字节码信息：" + classfileBuffer);
                        doGetClassSourceCode(classfileBuffer, className);
                        return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                    }
                };

                // 1. add transformer (true: allow transform)
                inst.addTransformer(transformer, true);

                // 2. trigger transformer
                try {
                    inst.retransformClasses(clazz);
                } catch (UnmodifiableClassException e) {
                    throw new RuntimeException(e);
                } finally {
                    inst.removeTransformer(transformer);
                }
            }
        }
    }

    public static void doGetClassSourceCode(byte[] classfileBuffer, String className) {
        try {
            // 1. 创建临时目录
            Path tempDir = Files.createTempDirectory("arthas-class-dump");

            // 2. 根据类名生成文件名
            String fileName = className.replace('.', '/') + ".class";
            File classFile = new File(tempDir.toFile(), fileName);

            // 3. 确保父目录存在
            classFile.getParentFile().mkdirs();

            // 4. 写入字节码到临时文件
            Files.write(classFile.toPath(), classfileBuffer);

            // 5. 调用反编译工具
            decompile(classFile.getAbsolutePath());

            // 7. 删除临时文件
            classFile.delete();
            tempDir.toFile().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decompile(String classFilePath) {
        // 1. result对象
        final StringBuilder result = new StringBuilder();

        // 2. 创建CFR驱动
        CfrDriver driver = new CfrDriver.Builder()
                .withOutputSink(new OutputSinkFactory() {
                    @Override
                    public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                        return Collections.singletonList(SinkClass.STRING);
                    }

                    @Override
                    public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
                        return data -> result.append(data);
                    }
                })
                .build();

        // 3. 执行反编译
        driver.analyse(Collections.singletonList(classFilePath));

        // 4. 返回结果
        System.out.println(result.toString());
    }
}
