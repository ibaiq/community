package com.ibaiq.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ibaiq.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作（Menu）表接口
 *
 * @author 十三
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 解决超级管理员可查到被软删除的菜单
     *
     * @return 菜单集合
     */
    List<Menu> getAll(Wrapper<Menu> ew);

}
