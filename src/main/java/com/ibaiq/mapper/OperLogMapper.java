package com.ibaiq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ibaiq.entity.OperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 十三
 */
@Mapper
public interface OperLogMapper extends BaseMapper<OperLog> {

    /**
     * 清空数据
     */
    void clear();

}
