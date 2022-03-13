package com.xhtt.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合工具类
 *
 * @Date 2019-02-13 下午 4:31
 */
public class ListUtils {


    /**
     * 集合分割成多个相同个数的集合
     *
     * @param source 待分割集合
     * @param n      元素个数
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();

        int sourceSize = source.size();
        int size = source.size() % n == 0 ? (source.size() / n) : (source.size() / n) + 1;

        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<T>();
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.add(source.get(j));
                }
            }
            result.add(subset);
        }

        return result;
    }

    /**
     * 比较2个list是否相等
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean listEqualsCheck(List<String> s1, List<String> s2) {
        if (s1 == s2) {
            return true;
        } else {
            if (CollectionUtils.isEmpty(s1) && CollectionUtils.isEmpty(s2)) {
                return true;
            }
            if (!CollectionUtils.isEmpty(s1) && !CollectionUtils.isEmpty(s2)) {
                if (s1.size() != s2.size()) {
                    return false;
                } else {
                    if (s1.containsAll(s2)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
