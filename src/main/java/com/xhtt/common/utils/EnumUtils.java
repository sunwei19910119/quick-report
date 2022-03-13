package com.xhtt.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举工具
 *
 * @Date 2019/2/21 11:08
 **/
public class EnumUtils {

    /**
     * 根据code获取msg
     *
     * @param clazz                 枚举类名
     * @param getTypeCodeMethodName 获取code的方法
     * @param typeCode              code
     */
    public static <T extends Enum<T>> T getMsgByCode(Class<T> clazz, String getTypeCodeMethodName, Integer typeCode) {
        T result = null;
        try {
            //Enum接口中没有values()方法，我们仍然可以通过Class对象取得所有的enum实例
            T[] arr = clazz.getEnumConstants();
            //获取定义的方法
            Method targetMethod = clazz.getDeclaredMethod(getTypeCodeMethodName);
            Integer typeCodeVal = null;
            //遍历枚举定义
            for (T entity : arr) {
                //获取传过来方法的
                typeCodeVal = Integer.valueOf(targetMethod.invoke(entity).toString());
                //执行的方法的值等于参数传过来要判断的值
                if (typeCodeVal.equals(typeCode)) {
                    //返回这个枚举
                    result = entity;
                    break;
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T extends Enum<T>> T getMsgByCode(Class<T> clazz, Integer typeCode) {
        return getMsgByCode(clazz, "getCode", typeCode);
    }
}
