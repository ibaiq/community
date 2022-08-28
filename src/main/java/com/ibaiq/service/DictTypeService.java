package com.ibaiq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.DictType;

/**
 * @author 十三
 */
public interface DictTypeService {

    void loadingDictCache();

    Page<DictType> selectDictTypeList(Integer pageNum, Integer pageSize, DictType dictType);

}
