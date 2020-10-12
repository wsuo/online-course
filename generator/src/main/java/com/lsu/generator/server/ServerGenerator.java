package com.lsu.generator.server;

import com.lsu.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 18:35
 * @Version 1.0
 */
public class ServerGenerator {

    private static String toServicePath = "server\\src\\main\\java\\com\\lsu\\server\\service\\";
    private static String toControllerPath = "business\\src\\main\\java\\com\\lsu\\business\\controller\\admin\\";

    public static void main(String[] args) throws IOException, TemplateException {
        String Domain = "Section";
        String domain = "section";
        Map<String, Object> map = new HashMap<>(2);
        map.put("Domain", Domain);
        map.put("domain", domain);
//        FreemarkerUtil.initConfig("service");
//        FreemarkerUtil.generator(map, toServicePath + Domain + "Service.java");
        FreemarkerUtil.initConfig("controller");
        FreemarkerUtil.generator(map, toControllerPath + Domain + "Controller.java");
    }
}
