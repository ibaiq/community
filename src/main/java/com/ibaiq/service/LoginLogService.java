package com.ibaiq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.LoginLog;

import java.util.List;

/**
 * @author 十三
 */
public interface LoginLogService {

    /**
     * 记录登录日志
     *
     * @param log 日志信息
     */
    void recording(LoginLog log);

    /**
     * 获取分页数据
     *
     * @param pageNum  页码
     * @param pageSize 数量
     */
    Page<LoginLog> getAll(String pageNum, String pageSize, LoginLog loginLog);

    /**
     * 批量删除日志
     */
    void delete(List<Integer> ids);

    /**
     * 清空日志
     */
    void clear();

}
