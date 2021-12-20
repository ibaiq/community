package com.ibaiq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.Role;
import com.ibaiq.entity.User;

import java.util.List;

/**
 * （Role）服务接口
 *
 * @author 十三
 */
public interface RoleService {

    /**
     * 查询一个角色
     *
     * @param id 角色id
     * @return 角色对象
     */
    Role findById(Integer id);

    /**
     * 查询指定角色拥有的菜单id
     *
     * @param id 角色id
     * @return 返回菜单id
     */
    List<Integer> getMenuIdsByIds(Integer id);

    /**
     * 获取所有角色
     *
     * @return 所有角色
     */
    List<Role> getAll();

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     */
    void modify(Role role);

    /**
     * 添加角色
     *
     * @param role 角色信息
     */
    void add(Role role);

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    void deleteById(List<Integer> id);

    /**
     * 分页查询
     *
     * @param pageNum  页码
     * @param pageSize 数量
     * @return 分页数据
     */
    Page<Role> getPagination(Integer pageNum, Integer pageSize, Role role);

    /**
     * 获取未授权用户的角色列表
     */
    Page<Role> getUnallocatedList(Integer pageNum, Integer pageSize);

    /**
     * 获取用户授权的角色
     *
     * @param userId 用户id
     */
    User getUserAuthRole(Integer userId);

    /**
     * 获取需要权限的角色标识
     * @return
     */
    List<String> getAuthRoleNames();

    /**
     * 异步批量删除角色
     *
     * @param ids 角色id集合
     */
    void asyncDelRole(List<Integer> ids);

}
