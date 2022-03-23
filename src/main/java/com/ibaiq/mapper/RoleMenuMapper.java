package com.ibaiq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ibaiq.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;
import java.util.List;

/**
 * 操作（RoleMenu）表接口
 *
 * @author 十三
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色id查询所有的菜单id
     *
     * @param roleIds 角色id
     * @return 菜单id集合
     */
    List<Integer> selectMenuIdsByRoleIds(List<Integer> roleIds);

    /**
     * 根据角色id查询菜单id
     *
     * @param roleId 角色id
     * @return 集合
     */
    HashSet<Integer> selectMenuIdsByRoleId(Integer roleId);

    /**
     * 根据菜单父id和角色id集合查询菜单id
     *
     * @param roleId  角色id
     * @param menuIds 菜单id集合
     */
    HashSet<Integer> selectMenuIdsNotInParentIds(Integer roleId, List<Integer> menuIds);

}
