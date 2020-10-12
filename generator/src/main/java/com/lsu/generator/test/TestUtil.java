package com.lsu.generator.test;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 测试工具类
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 18:14
 * @Version 1.0
 */
public class TestUtil {

    private static String ftlPath = "generator\\src\\main\\java\\com\\lsu\\generator\\test\\";
    private static String toPath = "generator\\src\\main\\java\\com\\lsu\\generator\\test\\";

    public static void main(String[] args) throws IOException, TemplateException {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_29);
        conf.setDirectoryForTemplateLoading(new File(ftlPath));
        conf.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
        Template temp = conf.getTemplate("test.ftl");

        FileWriter fw = new FileWriter(toPath + "Test.java");
        BufferedWriter bw = new BufferedWriter(fw);
        temp.process(null, bw);
        bw.flush();
        fw.close();
    }
}
