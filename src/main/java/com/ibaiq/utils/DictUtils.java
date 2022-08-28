package com.ibaiq.utils;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.entity.DictData;
import com.ibaiq.utils.spring.SpringUtils;

import java.util.List;

/**
 * @author 十三
 */
public class DictUtils {

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 设置字典缓存
     *
     * @param key      参数键
     * @param dictData 字典数据列表
     */
    public static void setDictCache(String key, List<DictData> dictData) {
        SpringUtils.getBean(RedisUtils.class).set(getCacheKey(key), dictData);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return Constants.REDIS_PREFIX_DICT_KEY + configKey;
    }

}
