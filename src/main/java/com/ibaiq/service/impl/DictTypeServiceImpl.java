package com.ibaiq.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.DictData;
import com.ibaiq.entity.DictType;
import com.ibaiq.mapper.DictTypeMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.DictTypeService;
import com.ibaiq.utils.DictUtils;
import com.ibaiq.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author 十三
 */
@Service
public class DictTypeServiceImpl extends BaseService<DictTypeMapper, DictType> implements DictTypeService {

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    @Override
    public void loadingDictCache() {
        LambdaQueryWrapper<DictData> dictDataQuery = new LambdaQueryWrapper<>();
        dictDataQuery.eq(DictData::getStatus, true);

        Map<String, List<DictData>> dictDataMap = dictDataMapper.selectList(dictDataQuery).stream().collect(Collectors.groupingBy(DictData::getDictType));
        for (Map.Entry<String, List<DictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(DictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    @Override
    public Page<DictType> selectDictTypeList(Integer pageNum, Integer pageSize, DictType dictType) {
        query.clear();

        Page<DictType> page = new Page<>(pageNum, pageSize);

        query.eq(StringUtils.isNotEmpty(dictType.getDictName()), DictType::getDictName, dictType.getDictName())
                          .eq(ObjectUtil.isNotNull(dictType.getStatus()), DictType::getStatus, dictType.getStatus())
                          .like(StringUtils.isNotEmpty(dictType.getDictType()), DictType::getDictType, dictType.getDictType())
                          .eq(DictType::getDeleted, false);

        return dictTypeMapper.selectPage(page, query);
    }

}
