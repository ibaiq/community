package com.ibaiq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作（Role）表接口
 *
 * @author 十三
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询用户用几个角色
     * 根据用户id查询到的所有角色id
     *
     * @param roleIds 角色id
     * @return 当前用户的所有角色集合
     */
    List<Role> selectUserRoles(List<Integer> roleIds);

    /**
     * 查询未授权用户的角色列表
     */
    Page<Role> selectUnallocatedList(Page<Role> page);

    @Select("SELECT `name` FROM `role` WHERE `id`  IN (1, 2) LIMIT 2")
    List<String> selectRoleNames();

}
