package com.ibaiq.security.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.service.RoleService;
import com.ibaiq.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author 十三
 */
@Component
@SuppressWarnings("all")
public class UrlAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    private RedisUtils redis;

    /**
     * @param authentication: 当前登录用户的角色信息
     * @param object:         请求url信息
     * @param collection:     `UrlFilterInvocationSecurityMetadataSource`中的getAttributes方法传来的，表示当前请求需要的角色（可能有多个）
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        // 获取实例
        FilterInvocation filterInvocation = (FilterInvocation) o;
        // 获取请求地址
        String url = filterInvocation.getRequestUrl();
        // 请求地址是否是后台api
        if (url.contains(Constants.MANAGE)) {
            // 获取动态权限标识
            List<String> list = redis.get(Constants.REDIS_KEY_SECURITY_ROLES, List.class);
            if (ObjectUtil.isEmpty(list)) {
                // 如果获取为null或者empty去数据库查询
                list = SpringUtil.getBean(RoleService.class).getAuthRoleNames();
            }
            // 获取当前用户的权限信息
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            final List<String> LIST = list;
            for (GrantedAuthority authority : authorities) {
                // 循环查找当前用户是否存在权限标识
                if (LIST.contains(authority.getAuthority())) {
                    // 存在放行
                    return;
                }
            }
            throw new AccessDeniedException(MessageEnum.PERMISSION_DENIED.getMsg());
        }
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
