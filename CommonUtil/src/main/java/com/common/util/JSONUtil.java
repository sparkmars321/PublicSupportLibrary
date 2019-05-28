package com.common.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class JSONUtil {

    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T parseObject(String text, Class<T> cls) {
        return JSON.parseObject(text, cls);
    }

    public static <T> List<T> parseArray(String text, Class<T> cls) {
        return JSON.parseArray(text, cls);
    }
}
