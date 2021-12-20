package com.ibaiq.service;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.entity.User;
import com.ibaiq.utils.RedisUtils;
import com.ibaiq.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 十三
 */
@Service
@Slf4j
public class PermissionService {

    @Autowired
    private RedisUtils redis;

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(User user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isSysAdmin()) {
            perms.add("*:*:*");
        } else {
            String authority = redis.get(Constants.REDIS_PREFIX_AUTHORITY + user.getUsername(), String.class);
            if (StringUtils.isNotEmpty(authority)) {
                String[] split = authority.split(",");

                Set<String> collect = Arrays.stream(split)
                                  .filter(auth -> auth.contains(":"))
                                  .collect(Collectors.toSet());

                perms.addAll(collect);
            }
        }
        return perms;
    }

    public Set<String> getMenuPermission(String authority) {
        Set<String> perms = new HashSet<>();

        if (StringUtils.isNotEmpty(authority)) {
            String[] split = authority.split(",");

            Set<String> collect = Arrays.stream(split)
                              .filter(auth -> auth.contains(":"))
                              .collect(Collectors.toSet());

            perms.addAll(collect);
        }

        return perms;
    }

}
