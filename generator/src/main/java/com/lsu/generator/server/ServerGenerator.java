package com.lsu.generator.server;

import com.lsu.generator.util.DbUtil;
import com.lsu.generator.util.Field;
import com.lsu.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.*;

/**
 * 代码生成
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 18:35
 * @Version 1.0
 */
public class ServerGenerator {

    private static String MODULE = "business";
    private static String toServicePath = "server\\src\\main\\java\\com\\lsu\\server\\service\\";
    private static String toDtoPath = "server\\src\\main\\java\\com\\lsu\\server\\dto\\";
    private static String toControllerPath = MODULE + "\\src\\main\\java\\com\\lsu\\" + MODULE + "\\controller\\admin\\";

    public static void main(String[] args) throws Exception {
        String bigDoMain = "Section";
        String domain = "section";
        String tableNameCn = "小节";
        String module = MODULE;
        List<Field> fieldList = DbUtil.getColumnByTableName(domain);
        Set<String> typeSet = getJavaTypes(fieldList);
        Map<String, Object> map = new HashMap<>(2);
        map.put("Domain", bigDoMain);
        map.put("domain", domain);
        map.put("tableNameCn", tableNameCn);
        map.put("module", module);
        map.put("fieldList", fieldList);
        map.put("typeSet", typeSet);

        // 生成 dto
        FreemarkerUtil.initConfig("dto");
        FreemarkerUtil.generator(map, toDtoPath + bigDoMain + "Dto.java");

        // 生成 service
        FreemarkerUtil.initConfig("service");
        FreemarkerUtil.generator(map, toServicePath + bigDoMain + "Service.java");

        // 生成 controller
        FreemarkerUtil.initConfig("controller");
        FreemarkerUtil.generator(map, toControllerPath + bigDoMain + "Controller.java");
    }

    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (Field field : fieldList) {
            set.add(field.getJavaType());
        }
        return set;
    }
}
