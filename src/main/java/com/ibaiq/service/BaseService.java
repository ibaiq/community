package com.ibaiq.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ibaiq.common.Common;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.converter.UserConverter;
import com.ibaiq.entity.RoleMenu;
import com.ibaiq.entity.RoleUser;
import com.ibaiq.entity.User;
import com.ibaiq.entity.UserOnline;
import com.ibaiq.mapper.*;
import com.ibaiq.service.async.AsyncService;
import com.ibaiq.utils.RedisUtils;
import com.ibaiq.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基本服务类
 *
 * @author 十三
 */
public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    @Autowired
    protected IbaiqConfig ibaiq;

    @Autowired
    protected AsyncService async;

    @Autowired
    protected HttpServletRequest request;

    /**
     * redis工具类
     */
    @Autowired
    protected RedisUtils redis;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected RoleMapper roleMapper;

    @Autowired
    protected MenuMapper menuMapper;

    @Autowired
    protected RoleUserMapper roleUserMapper;

    @Autowired
    protected RoleMenuMapper roleMenuMapper;

    @Autowired
    protected LoginLogMapper loginLogMapper;

    @Autowired
    protected SysConfigMapper sysConfigMapper;

    @Autowired
    protected OperLogMapper operLogMapper;

    @Autowired
    protected AccessLogMapper accessLogMapper;

    @Autowired
    protected DictTypeMapper dictTypeMapper;

    @Autowired
    protected DictDataMapper dictDataMapper;

    @Autowired
    protected TokenUtils tokenUtils;

    @Autowired
    protected Common common;

    @Autowired
    protected UserConverter userConverter;

    /**
     * 泛型的lambda查询构造器
     */
    protected final LambdaQueryWrapper<T> query = new LambdaQueryWrapper<>();

    /**
     * 泛型的lambda更新构造器
     */
    protected final LambdaUpdateWrapper<T> update = new LambdaUpdateWrapper<>();

    public void clearUserAuthorityInfo(String username) {
        redis.del(Constants.REDIS_PREFIX_AUTHORITY + username);
    }

    public void clearUserAuthorityInfo(String[] keys) {
        redis.del(keys);
    }

    @Async("taskExecutor")
    public void clearUserAuthorityInfoByRoleId(Integer roleId) {
        ArrayList<Integer> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        clearUserAuthorityInfoByRoleIds(roleIds);
    }

    @Async("taskExecutor")
    public void clearUserAuthorityInfoByRoleIds(List<Integer> roleIds) {
        List<Integer> userIds = roleUserMapper.selectList(new LambdaQueryWrapper<RoleUser>().in(RoleUser::getRoleId, roleIds))
                          .stream().map(RoleUser::getUserId).collect(Collectors.toList());

        if (userIds.size() > 0) {
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getId, userIds));
            // 清除多个用户的权限缓存
            String[] array = users.stream().map(user -> Constants.REDIS_PREFIX_AUTHORITY + user.getUsername()).toArray(String[]::new);
            clearUserAuthorityInfo(array);
        }
    }

    @Async("taskExecutor")
    public void clearUserAuthorityInfoByMenuId(Integer menuId) {
        // 根据菜单id获取所有角色id
        List<Integer> roleIds = roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId, menuId))
                          .stream().map(RoleMenu::getRoleId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            // 再用角色id获取用户id
            List<Integer> userIds = roleUserMapper.selectList(new LambdaQueryWrapper<RoleUser>().in(RoleUser::getRoleId, roleIds))
                              .stream().map(RoleUser::getUserId).collect(Collectors.toList());
            // 然后用多个用户id获取多个用户
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getId, userIds));
            // 清除多个用户的权限缓存
            users.forEach(user -> this.clearUserAuthorityInfo(user.getUsername()));
        }
    }

    public void updateRedisCache(User user) {
        Collection<String> keys = redis.keys(Constants.REDIS_PREFIX_TOKEN + "*");
        keys.forEach(key -> {
            UserOnline online = redis.get(key, UserOnline.class);
            if (online.getUser().getId().equals(user.getId())) {
                online.setUsername(user.getUsername());
                online.setUser(user);
                redis.set(key, online, redis.getExpire(key));
            }
        });
    }

}
