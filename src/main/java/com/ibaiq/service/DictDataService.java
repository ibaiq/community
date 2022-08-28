package com.ibaiq.service;

import com.ibaiq.entity.DictData;

import java.util.List;

/**
 * @author 十三
 */
public interface DictDataService {

    List<DictData> selectDictDataList(DictData dictData);

    List<DictData> selectDictDataByType(String dictType);

}
