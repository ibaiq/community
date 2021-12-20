package com.ibaiq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.SysConfig;

/**
 * @author 十三
 */
@SuppressWarnings("all")
public interface SysConfigService {

    /**
     * 初始化
     */
    public void init();

    /**
     * 修改系统配置内容
     *
     * @param config 新的内容
     */
    void modifyConfig(SysConfig config);

    /**
     * 根据唯一键查询配置信息
     *
     * @param key 键
     */
    SysConfig find(String key);

    /**
     * 添加配置信息
     *
     * @param config 配置信息
     */
    void add(SysConfig config);

    /**
     * 根据唯一键查询配置内容
     *
     * @param key 键
     * @return
     */
    String getKeyValue(String key);

    /**
     * 获取分页数据可有条件
     *
     * @param pageNum  页码
     * @param pageSize 数量
     * @param config   条件信息
     * @return
     */
    Page<SysConfig> list(String pageNum, String pageSize, SysConfig config);

}
