package com.xhtt.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class EntityConverter {

    /**
     * 实体类转换 (若covertEntity是一个已被赋值的实体转换时会替换相同属性名)
     * <p>
     * 根据实体内属性名进行映射
     * <p>
     * 属性名相同 默认属性名相同 则认为属性类型也相同 且不为空  则映射
     *
     * @param entity       被转换实体
     * @param covertEntity 转换后的实体
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <S extends T, T> S converEntity(S entity, S covertEntity) throws IllegalArgumentException, IllegalAccessException {
        Field[] entityfields = entity.getClass().getDeclaredFields();
        Field[] covertfields = covertEntity.getClass().getDeclaredFields();
        boolean breakFlag = false;
        for (Field covertfield : covertfields) {
            covertfield.setAccessible(true);
            for (Field entityfield : entityfields) {
                entityfield.setAccessible(true);
                if (entityfield.getName().equals(covertfield.getName())) {//属性名相同
                    if (entityfield.get(entity) != null) {//如果不为空 则放入
                        if (Modifier.isFinal(entityfield.getModifiers())) {//如果属性是final类型，不赋值
                            break;
                        }
                        covertfield.set(covertEntity, entityfield.get(entity));
                        breakFlag = true;
                    }
                }
                if (breakFlag) {
                    breakFlag = false;
                    break;
                }
            }

        }

        return covertEntity;
    }


    /**
     * 实体类转换
     * <p>
     * 根据实体内属性名进行映射
     * <p>
     * 属性名相同 默认属性名相同 则认为属性类型也相同 且不为空  则映射
     *
     * @param <T>
     * @param <S>
     * @param entity            被转换实体
     * @param covertEntityClass 转换后的实体类型
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T, S> T entityConver(S entity, Class<T> covertEntityClass) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        T covertEntity = covertEntityClass.newInstance();
        Field[] entityfields = entity.getClass().getDeclaredFields();
        Field[] covertfields = covertEntityClass.getDeclaredFields();
        boolean breakFlag = false;
        for (Field covertfield : covertfields) {
            covertfield.setAccessible(true);
            for (Field entityfield : entityfields) {
                entityfield.setAccessible(true);
                if (entityfield.getName().equals(covertfield.getName())) {//属性名相同
                    if (entityfield.get(entity) != null) {//如果不为空 则放入
                        if ((entityfield.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {//如果属性是final类型，不赋值
                            break;
                        }
                        covertfield.set(covertEntity, entityfield.get(entity));
                        breakFlag = true;
                    }
                }
                if (breakFlag) {
                    breakFlag = false;
                    break;
                }
            }

        }

        return covertEntity;
    }

    /**
     * 转换list
     *
     * @param entities          要转换的list
     * @param covertEntityClass 需要转换的 class
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T, S> List<T> listConver(List<S> entities, Class<T> covertEntityClass) throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        List<T> covertEntitys = new ArrayList<T>();
        for (S entity : entities) {
            covertEntitys.add(entityConver(entity, covertEntityClass));
        }
        return covertEntitys;
    }


//    /**
//     * @param entity      操作对象
//     * @param entityClass 操作类，用于获取类中的方法
//     * @return
//     * @throws IllegalAccessException
//     * @throws IllegalArgumentException
//     * @MethodName : getUpdate
//     * @Description : 获取类中所有属性及属性值 获取update 对象
//     */
//    public Update getUpdate(Object entity, Class<?> entityClass) throws IllegalArgumentException, IllegalAccessException {
//        Update update = new Update();
//        // 获取类中的所有定义字段
//        Field[] fields = entityClass.getDeclaredFields();
//
//        // 循环遍历字段，获取字段对应的属性值
//        for (Field field : fields) {
//            // 如果不为空，设置可见性，然后返回
//            field.setAccessible(true);
//            if (!"serialVersionUID".equals(field.getName())) {
//                if (field.get(entity) != null) {
//                    if (isNeedPass(field)) {// 无带有Transient标示或者主键
//                        update.set(field.getName(), field.get(entity));
//                    }
//                }
//            }
//        }
//        return update;
//    }


//    /**
//     * 判断是否有 类似 Transient 这种需要跳过的注解
//     *
//     * @param field
//     * @return
//     */
//    private Boolean isNeedPass(Field field) {
//        Boolean flag = true;
//        Annotation[] annotations = field.getAnnotations();
//        if (annotations.length > 0) {
//            for (Annotation annotation : annotations) {
//                if ("interface org.springframework.data.annotation.Transient".equals(annotation.getClass())) {
//                    flag = false;
//                    break;
//                }
//            }
//        }
//
//        return flag;
//    }

}
