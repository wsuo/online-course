package com.lsu.server.util;

import com.lsu.server.exception.ValidatorException;
import org.springframework.util.StringUtils;

/**
 * 校验类
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 8:26
 * @Version 1.0
 */
public class ValidatorUtil {

    public static void require(String str, String fieldName) {
        if (StringUtils.isEmpty(str)) {
            throw new ValidatorException(fieldName + "不能为空");
        }
    }

    public static void length(String str, String fieldName, int min, int max) {
        if (StringUtils.isEmpty(str)) {
            return;
        }
        int length = 0;
        if (!StringUtils.isEmpty(str)) {
            length = str.length();
        }
        if (length < min || length > max) {
            throw new ValidatorException(fieldName + "长度" + min + "~" + max + "位");
        }
    }
}
