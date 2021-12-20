package com.ibaiq.service;

import com.ibaiq.entity.RoleMenu;

import java.util.List;

/**
 * （RoleMenu）服务接口
 *
 * @author 十三
 */
public interface RoleMenuService {

    /**
     * 角色分配权限
     *
     * @param roleId    角色id
     * @param roleMenus 分配的集合
     */
    void modify(Integer roleId, List<RoleMenu> roleMenus);

    void selectMenu(Integer roleId, List<Integer> menuIds);

}
