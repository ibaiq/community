package com.ibaiq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作（LoginLog）表接口
 *
 * @author 十三
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    Page<LoginLog> selectPages(Page<LoginLog> page, LoginLog log);

    void clear();

}
