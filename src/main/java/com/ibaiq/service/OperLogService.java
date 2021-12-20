package com.ibaiq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.OperLog;

import java.util.List;

/**
 * @author 十三
 */
public interface OperLogService {

    /**
     * 获取分页数据
     */
    Page<OperLog> list(Integer pageNum, Integer pageSize, OperLog operations);

    /**
     * 删除一条日志
     */
    void deleteById(List<Integer> ids);

    /**
     * 清空日志
     */
    void clear();

}
