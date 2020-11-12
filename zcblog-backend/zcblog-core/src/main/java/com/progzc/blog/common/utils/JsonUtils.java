package com.progzc.blog.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Description Json字符串与Object互相转换的工具类
 * @Author zhaochao
 * @Date 2020/11/3 20:24
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */

@Slf4j
public class JsonUtils {

    private static ObjectMapper objMapper = new ObjectMapper();

    /**
     * Json字符串转换为Object（不包含集合）
     * @param jsonString 转换前的字符串
     * @param clazz      转换后的Class对象
     * @param <T>        转换后的对象类型
     * @return 转换后的对象
     */
    public static <T> T toObj(String jsonString, Class<T> clazz) {
        objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return objMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.error("Json字符串转换为对象出错", e);
        }
        return null;
    }

    /**
     * Object（包含集合）转换为Json字符串
     * @param obj 转换前的对象
     * @return 转换后的Json字符串
     */
    public static String toJson(Object obj) {
        if (obj instanceof Integer || obj instanceof Long || obj instanceof Float ||
                obj instanceof Double || obj instanceof Boolean || obj instanceof String) {
            return String.valueOf(obj);
        }
        try {
            return objMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转换为Json字符串出错", e);
        }
        return null;
    }

    /**
     * json字符串转化为Collection<JavaBean>
     * @param jsonString      json字符串
     * @param collectionClass Collection的类型
     * @param elementClasses  JavaBean的类型
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> T toObjArray(String jsonString, Class<T> collectionClass, Class<E>... elementClasses) {
        try {
            JavaType javaType = objMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return objMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            log.error("json字符串转化为集合出错", e);
        }
        return null;
    }
}
