package com.ibaiq.web.service;

import com.ibaiq.entity.User;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.spring.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * @author 十三
 */
@Service("permission")
public class PermissionService {

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermits(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        User user = SecurityUtils.getUser().getUser();
        if (StringUtils.isNull(user) || CollectionUtils.isEmpty(user.getPermissions())) {
            return false;
        }
        return user.isSysAdmin() || hasPermissions(user.getPermissions(), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }

}
