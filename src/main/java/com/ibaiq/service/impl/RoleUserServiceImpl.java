package com.ibaiq.service.impl;

import com.ibaiq.entity.RoleUser;
import com.ibaiq.entity.User;
import com.ibaiq.manager.AsyncManager;
import com.ibaiq.manager.factory.AsyncFactory;
import com.ibaiq.mapper.RoleUserMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.PermissionService;
import com.ibaiq.service.RoleUserService;
import com.ibaiq.service.UserService;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * （RoleUser）表业务实现类
 *
 * @author 十三
 */
@Slf4j
@Service
public class RoleUserServiceImpl extends BaseService<RoleUserMapper, RoleUser> implements RoleUserService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authorize(Integer userId, List<RoleUser> roleUsers) {
        query.clear();
        query.eq(RoleUser::getUserId, userId);
        this.remove(query);
        this.saveBatch(roleUsers);

        // 删除缓存
        User user = userMapper.selectById(userId);
        clearUserAuthorityInfo(user.getUsername());

        // 异步添加缓存
        AsyncManager.me().execute(AsyncFactory.asyncInvokeMethod("getUserAuthorityInfo", userService.getClass(), user));
    }

    @Override
    public void updateCache(User user, String authority) {
        user = common.setRoleIdsAndRoles(user);
        if (StringUtils.isNotBlank(authority)) {
            user.setPermissions(SpringUtils.getBean(PermissionService.class).getMenuPermission(authority));
        } else {
            user.setPermissions(SpringUtils.getBean(PermissionService.class).getMenuPermission(user));
        }
        super.updateRedisCache(user);
    }

    @Override
    public void selectAuthorize(Integer roleId, List<Integer> userIds) {
        if (userIds.isEmpty()) {
            return;
        }
        userIds.remove(1);
        roleUserMapper.batchDeleteByUserIdList(roleId, userIds);
        List<RoleUser> roleUsers = new ArrayList<>(Collections.emptyList());
        userIds.forEach(userId -> {
            RoleUser roleUser = new RoleUser();
            roleUser.setRoleId(roleId);
            roleUser.setUserId(userId);
            roleUsers.add(roleUser);
        });
        this.saveBatch(roleUsers);
    }

    @Override
    public void cancelAuthorize(Integer roleId, List<Integer> userIds) {
        if (userIds.isEmpty()) {
            return;
        }
        userIds.remove(Integer.valueOf(1));
        roleUserMapper.batchDeleteByUserIdList(roleId, userIds);
    }

}
