package com.xhtt.common.utils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.springframework.stereotype.Component;

@Component
public class TimedCacheUtil {
    TimedCache<String, String> timedCache;

    TimedCacheUtil() {
        //创建缓存，默认4毫秒过期
        timedCache = CacheUtil.newTimedCache(300000);//默认过期(5分钟)
        //启动定时任务，每5毫秒秒检查一次过期
        timedCache.schedulePrune(100);
    }

    public void set(String key, String value) {
        timedCache.put(key, value);
    }

    public void set(String key, String value, Integer second) {
        timedCache.put(key, value, 1000 * second);
    }

    public String get(String key) {
        return timedCache.get(key, false);
    }
}
