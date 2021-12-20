package com.ibaiq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ibaiq.entity.RoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作（RoleUser）表接口
 *
 * @author 十三
 */
@Mapper
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    /**
     * 根据用户id查询所有角色id
     *
     * @param userId 用户id
     * @return 角色id集合
     */
    @Select("SELECT role_id FROM role_user WHERE user_id = #{userId}")
    List<Integer> selectRoleIdList(Integer userId);

    /**
     * 根据角色id查询授权的用户id
     */
    @Select("SELECT user_id FROM role_user WHERE role_id = #{roleId}")
    List<Integer> selectUserIdList(Integer roleId);

    /**
     * 查询所有授权的用户id
     */
    @Select("SELECT user_id FROM role_user WHERE user_id IS NOT NULL GROUP BY user_id")
    List<Integer> selectUserIds();

    /**
     * 按用户 ID 列表批量删除
     */
    void batchDeleteByUserIdList(Integer roleId, List<Integer> userIds);

    /**
     * 批量删除关联信息
     */
    void batchDeleteByRoleIds(List<Integer> roleIds);
}
