package com.ibaiq.service.impl;

import com.ibaiq.entity.LoginUser;
import com.ibaiq.entity.User;
import com.ibaiq.mapper.UserMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * （UserDetailsService）实现类
 *
 * @author 十三
 */
@Slf4j
@Service
public class UserDetailsServiceImpl extends BaseService<UserMapper, User> implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * 自定义登录查询数据库
     *
     * @param username 用户名
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        query.clear();
        // 数据库查询条件
        query.eq(User::getUsername, username);
        // 查询结果
        User user = userMapper.selectOne(query);

        boolean isNotLocked = user.getStatus();
        boolean isEnabled = user.getDeleted() == 0;

        return new LoginUser(user, user.getId(), user.getUsername(), user.getPassword(),
                          true, isNotLocked, true, isEnabled,
                          getUserAuthority(user));
    }

    private List<GrantedAuthority> getUserAuthority(User user) {
        String authority = userService.getUserAuthorityInfo(user);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }

}
