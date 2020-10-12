package com.lsu.generator.server;

import com.lsu.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * 代码生成
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 18:35
 * @Version 1.0
 */
public class ServerGenerator {

    private static String toPath = "generator\\src\\main\\java\\com\\lsu\\generator\\test\\";

    public static void main(String[] args) throws IOException, TemplateException {
        FreemarkerUtil.initConfig("test");
        FreemarkerUtil.generator(toPath + "Test.java");
    }
}
