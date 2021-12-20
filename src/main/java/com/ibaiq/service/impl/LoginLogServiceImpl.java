package com.ibaiq.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.LoginLog;
import com.ibaiq.mapper.LoginLogMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 十三
 */
@Slf4j
@Service
public class LoginLogServiceImpl extends BaseService<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public void recording(LoginLog loginLog) {
        loginLogMapper.insert(loginLog);
    }

    @Override
    public Page<LoginLog> getAll(String pageNum, String pageSize, LoginLog loginLog) {
        Page<LoginLog> page = new Page<>(Long.parseLong(pageNum), Long.parseLong(pageSize));
        return loginLogMapper.selectPages(page, loginLog);
    }

    @Override
    public void delete(List<Integer> ids) {
        loginLogMapper.deleteBatchIds(ids);
    }

    @Override
    public void clear() {
        loginLogMapper.clear();
    }

}
