package com.ibaiq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.OperLog;
import com.ibaiq.mapper.OperLogMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.OperLogService;
import com.ibaiq.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 十三
 */
@Service
public class OperLogServiceImpl extends BaseService<OperLogMapper, OperLog> implements OperLogService {

    @Override
    public Page<OperLog> list(Integer pageNum, Integer pageSize, OperLog operations) {
        LambdaQueryWrapper<OperLog> query = new LambdaQueryWrapper<>();
        Page<OperLog> page = new Page<>(pageNum, pageSize);

        // 条件
        query.eq(StringUtils.isNotEmpty(operations.getModule()), OperLog::getModule, operations.getModule())
                          .lt(StringUtils.isNotNull(operations.getBeginTime()), OperLog::getOperTime, operations.getBeginTime())
                          .gt(StringUtils.isNotNull(operations.getEndTime()), OperLog::getOperTime, operations.getEndTime())
                          .orderByDesc(OperLog::getId);

        return operLogMapper.selectPage(page, query);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        operLogMapper.deleteBatchIds(ids);
    }

    @Override
    public void clear() {
        operLogMapper.clear();
    }
}
