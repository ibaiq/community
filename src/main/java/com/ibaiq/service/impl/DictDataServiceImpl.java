package com.ibaiq.service.impl;

import com.ibaiq.entity.DictData;
import com.ibaiq.exception.ParamIsNullException;
import com.ibaiq.mapper.DictDataMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.DictDataService;
import com.ibaiq.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 十三
 */
@Service
public class DictDataServiceImpl extends BaseService<DictDataMapper, DictData> implements DictDataService {

    @Override
    public List<DictData> selectDictDataList(DictData dictData) {
        query.clear();

        query.eq(StringUtils.isNotEmpty(dictData.getDictType()), DictData::getDictType, dictData.getDictType())
                          .like(StringUtils.isNotEmpty(dictData.getDictLabel()), DictData::getDictLabel, dictData.getDictLabel())
                          .eq(DictData::getDeleted, false);

        return dictDataMapper.selectList(query);
    }

    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        query.clear();

        if (StringUtils.isEmpty(dictType)) {
            throw new ParamIsNullException();
        }
        query.eq(DictData::getDictType, dictType)
                          .eq(DictData::getDeleted, false);
        return dictDataMapper.selectList(query);
    }

}
