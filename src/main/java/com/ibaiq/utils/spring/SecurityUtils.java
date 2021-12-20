package com.ibaiq.utils.spring;

import com.ibaiq.entity.UserOnline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security工具类
 *
 * @author 十三
 */
@SuppressWarnings("all")
@Slf4j
public class SecurityUtils {

    /**
     * 获取用户名
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 获取用户信息
     */
    public static UserOnline getUser() {
        return (UserOnline) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
