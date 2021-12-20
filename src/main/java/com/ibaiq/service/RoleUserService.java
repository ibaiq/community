package com.ibaiq.service;

import com.ibaiq.entity.RoleUser;
import com.ibaiq.entity.User;

import java.util.List;

/**
 * （RoleUser）服务接口
 *
 * @author 十三
 */
public interface RoleUserService {

    /**
     * 授权
     *
     * @param userId    用户id
     * @param roleUsers 用户角色对象集合
     */
    void authorize(Integer userId, List<RoleUser> roleUsers);

    /**
     * 更新缓存
     */
    void updateCache(User user, String authority);

    /**
     * 批量选择授权
     */
    void selectAuthorize(Integer roleId, List<Integer> userIds);

    /**
     * 批量取消授权
     */
    void cancelAuthorize(Integer roleId, List<Integer> userIds);
}
