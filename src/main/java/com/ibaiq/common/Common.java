package com.ibaiq.common;

import com.ibaiq.entity.Role;
import com.ibaiq.entity.User;
import com.ibaiq.mapper.RoleMapper;
import com.ibaiq.mapper.RoleUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 十三
 */
@Component
public class Common {

    @Autowired
    protected RoleMapper roleMapper;

    @Autowired
    protected RoleUserMapper roleUserMapper;


    public User setRoleIdsAndRoles(User user) {
        List<Integer> roleIds = roleUserMapper.selectRoleIdList(user.getId());
        if (roleIds != null && !roleIds.isEmpty()) {
            user = setRoleIdsAndRoles(user, roleIds, roleMapper.selectUserRoles(roleIds));
        }
        return user;
    }

    public User setRoleIdsAndRoles(User user, List<Integer> roleIds, List<Role> roles) {
        user.setRoleIds(roleIds);
        user.setRoles(roles);
        return user;
    }

}
