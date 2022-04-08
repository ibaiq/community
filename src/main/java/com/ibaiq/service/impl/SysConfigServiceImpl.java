package com.ibaiq.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.entity.SysConfig;
import com.ibaiq.exception.BaseException;
import com.ibaiq.mapper.SysConfigMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.SysConfigService;
import com.ibaiq.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * @author 十三
 */
@Service
@Slf4j
public class SysConfigServiceImpl extends BaseService<SysConfigMapper, SysConfig> implements SysConfigService {

    @Override
    @PostConstruct
    public void init() {
        List<SysConfig> list = sysConfigMapper.selectList(null);
        list.forEach(sysConfig -> redis.set(Constants.REDIS_PREFIX_CONFIG + sysConfig.getConfigKey(), sysConfig));
    }

    @Override
    public void modifyConfig(SysConfig config) {
        query.clear();
        query.eq(SysConfig::getId, config.getId());

        if ("".equals(config.getConfigKey())) {
            throw new BaseException(MessageEnum.CONFIG_KEY_IS_BLANK);
        }

        SysConfig sysConfig = sysConfigMapper.selectOne(query);
        SysConfig key = find(config.getConfigKey());

        if (StringUtils.isNotNull(sysConfig) && StringUtils.isNotNull(key)) {
            if (!sysConfig.getId().equals(key.getId())) {
                throw new BaseException(MessageEnum.CONFIG_IS_EXIST);
            }
        }

        sysConfigMapper.updateById(config);
        clearCache();
        async.configInitial();
    }

    @Override
    public SysConfig find(String key) {
        query.clear();
        SysConfig sysConfig = redis.get(Constants.REDIS_PREFIX_CONFIG + key, SysConfig.class);
        if (ObjectUtil.isNotNull(sysConfig)) {
            return sysConfig;
        }
        sysConfig = sysConfigMapper.selectOne(query.eq(SysConfig::getConfigKey, key));
        redis.set(Constants.REDIS_PREFIX_CONFIG + key, sysConfig);
        return sysConfig;
    }

    @Override
    public void add(SysConfig config) {
        if (StringUtils.isNotNull(find(config.getConfigKey()))) {
            throw new BaseException(MessageEnum.CONFIG_IS_EXIST);
        }
        sysConfigMapper.insert(config);
        async.configInitial();
    }

    @Override
    public String getKeyValue(String key) {
        return find(key).getConfigValue();
    }

    @Override
    public Page<SysConfig> list(String pageNum, String pageSize, SysConfig config) {
        LambdaQueryWrapper<SysConfig> query = new LambdaQueryWrapper<>();
        Page<SysConfig> page = new Page<>(Long.parseLong(pageNum), Long.parseLong(pageSize));

        // 条件
        query.like(StringUtils.isNotEmpty(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey());
        query.like(StringUtils.isNotEmpty(config.getConfigName()), SysConfig::getConfigName, config.getConfigName());
        query.eq(StringUtils.isNotNull(config.getConfigType()), SysConfig::getConfigType, config.getConfigType());
        query.gt(StringUtils.isNotNull(config.getBeginTime()), SysConfig::getCreated, config.getBeginTime());
        query.lt(StringUtils.isNotNull(config.getEndTime()), SysConfig::getCreated, config.getEndTime());

        return sysConfigMapper.selectPage(page, query);
    }

    public void clearCache() {
        Collection<String> keys = redis.keys(Constants.REDIS_PREFIX_CONFIG + "*");
        keys.forEach(key -> redis.del(key));
    }

}
