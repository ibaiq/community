package com.ibaiq.service;

import com.ibaiq.entity.Menu;
import com.ibaiq.entity.vo.RouterVo;

import java.util.List;

/**
 * （Menu）服务接口
 *
 * @author 十三
 */
public interface MenuService {

    /**
     * 获取当前用户可操作的菜单
     *
     * @return 菜单集合
     */
    List<Menu> getCurrentUserNav();

    /**
     * 获取所有菜单
     *
     * @return 菜单集合
     */
    List<Menu> getAll(Menu menu);

    /**
     * 获取一个菜单信息
     *
     * @param id 菜单id
     * @return 菜单信息
     */
    Menu findById(Integer id);

    /**
     * 添加菜单
     *
     * @param menu 菜单信息
     */
    void add(Menu menu);

    /**
     * 删除菜单
     *
     * @param id 菜单id
     */
    void deleteById(Integer id);

    /**
     * 修改菜单内容
     *
     * @param menu 修改的内容
     */
    void modify(Menu menu);

    /**
     * 构建路由
     */
    List<RouterVo> buildRouter(List<Menu> menus);

    /**
     * 获取已删除的菜单列表
     */
    List<Menu> getDeleted();

    /**
     * 恢复删除的菜单
     */
    void recover(Integer menuId);

}
