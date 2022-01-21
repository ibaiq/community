package com.ibaiq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.entity.Menu;
import com.ibaiq.entity.Role;
import com.ibaiq.entity.User;
import com.ibaiq.entity.UserOnline;
import com.ibaiq.exception.BaseException;
import com.ibaiq.exception.ParamIsNullException;
import com.ibaiq.exception.UserExistException;
import com.ibaiq.exception.UserNotExistException;
import com.ibaiq.manager.AsyncManager;
import com.ibaiq.manager.factory.AsyncFactory;
import com.ibaiq.mapper.UserMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.RoleUserService;
import com.ibaiq.service.UserService;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.encryption.MD5Utils;
import com.ibaiq.utils.spring.SecurityUtils;
import com.ibaiq.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * （User）表业务实现类
 *
 * @author 十三
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseService<UserMapper, User> implements UserService {

    @Override
    public User findById(Integer id) {
        if (id == null) {
            return null;
        }
        query.clear();
        User user = userMapper.selectOne(query.eq(User::getId, id)
                          .eq(User::getDeleted, 0)
                          .or()
                          .eq(SecurityUtils.getUser().getUser().isAdmin() && SecurityUtils.getUser().getUser().getPermissions().contains("sys:user:del:list"), User::getDeleted, 1)
                          .last("limit 1"));
        if (user == null) {
            throw new UserNotExistException(MessageEnum.USER_NOT_EXIST);
        }
        return user;
    }

    @Override
    public void add(User info) {
        query.clear();
        // 判断用户名是否存在
        Long count = userMapper.selectCount(query.eq(User::getUsername, info.getUsername()));
        if (count > 0) {
            throw new UserExistException(MessageEnum.USER_IS_EXIST);
        }
        // 密码加密
        info.setPassword(MD5Utils.getMD5(info.getPassword()));
        // 插入用户数据
        userMapper.insert(info);
    }

    @Override
    public Page<User> getAll(String pageNum, String pageSize, User user, boolean isDelete) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        Page<User> page = new Page<>(Long.parseLong(pageNum), Long.parseLong(pageSize));

        query.like(StringUtils.isNotEmpty(user.getUsername()), User::getUsername, user.getUsername());
        query.eq(StringUtils.isNotEmpty(user.getSex()), User::getSex, user.getSex());
        query.eq(StringUtils.isNotNull(user.getStatus()), User::getStatus, user.getStatus());
        query.like(StringUtils.isNotEmpty(user.getNickname()), User::getNickname, user.getNickname());
        query.ge(StringUtils.isNotNull(user.getBeginTime()), User::getCreated, user.getBeginTime());
        query.le(StringUtils.isNotNull(user.getEndTime()), User::getCreated, user.getEndTime());

        if (isDelete) {
            query.eq(User::getDeleted, 1);
        } else {
            query.eq(User::getDeleted, 0);
        }

        return userMapper.selectPage(page, query);
    }

    @Override
    public void modifyAvatar(Integer id, String url, HttpServletRequest request) {
        if (id == null) {
            throw new ParamIsNullException();
        }

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new UserNotExistException(MessageEnum.USER_NOT_EXIST);
        }

        user.setAvatar(url);
        user.setDeleted(0);
        user.setUpdated(new Date());
        userMapper.updateById(user);
        async.user_updateCache(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(User user) {
        if (user.isSysAdmin()) {
            throw new BaseException(MessageEnum.USER_IS_ADMIN);
        }
        if (StringUtils.isNotEmpty(user.getUsername())) {
            query.clear();
            query.eq(User::getUsername, user.getUsername());
            User res = userMapper.selectOne(query);
            if (StringUtils.isNotNull(res) && !user.getId().equals(res.getId())) {
                throw new BaseException(MessageEnum.USER_IS_EXIST);
            }
        }
        userMapper.updateById(user);
        async.user_updateCache(user.getId());
    }

    @Override
    public User getInfo() {
        return SecurityUtils.getUser().getUser();
    }

    @Override
    public String getUserAuthorityInfo(User user) {
        // 权限变量
        String authority = "";
        if (redis.hasKey(Constants.REDIS_PREFIX_AUTHORITY + user.getUsername())) {
            authority = String.valueOf(redis.get(Constants.REDIS_PREFIX_AUTHORITY + user.getUsername()));
        } else {
            // 获取用户授权的角色id
            List<Integer> roleIds = roleUserMapper.selectRoleIdList(user.getId());
            if (roleIds.size() > 0) {
                // 获取所授权的角色
                List<Role> roles = roleMapper.selectBatchIds(roleIds);
                if (roles.size() > 0) {
                    // 将所有角色拼接在一个字符串
                    authority = roles.stream().map(Role::getName).collect(Collectors.joining(","));
                }
                // 分布式获取所有菜单id
                List<Integer> menuIds = roleMenuMapper.selectMenuIdsByRoleIds(roleIds);
                if (menuIds.size() > 0) {
                    // 获取该用户下的有权限的菜单
                    List<Menu> menus = menuMapper.selectBatchIds(menuIds);
                    String menuPerms = menus.stream().map(Menu::getPerms).collect(Collectors.joining(","));
                    authority = authority.concat(",").concat(menuPerms);
                }
                // 异步更新在线用户的缓存信息
                async.user_updateCache(user, authority);
                AsyncManager.me().execute(AsyncFactory.asyncSetCache(Constants.REDIS_PREFIX_AUTHORITY + user.getUsername(), authority, Constants.REDIS_EXPIRED));
            }
        }

        return authority;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delOrRestById(List<Integer> userIds, boolean isDel) {
        if (userIds.isEmpty()) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        if ((userIds.size() == 1) && userIds.contains(1)) {
            throw new BaseException(MessageEnum.USER_IS_ADMIN);
        }
        userIds.remove(Integer.valueOf(1));
        if (isDel) {
            userMapper.deleteBatchById(userIds);
        } else {
            userMapper.recoverBatchById(userIds);
        }
        async.user_batchUpdateCache(userIds);
    }

    @Override
    public void batchUpdateCache(List<Integer> userIds) {
        List<User> users = userMapper.selectBatchIds(userIds);
        Collection<String> keys = redis.keys(Constants.REDIS_PREFIX_TOKEN + "*");
        keys.forEach(key -> {
            UserOnline online = redis.get(key, UserOnline.class);
            users.forEach(user -> {
                if (user.getId().equals(online.getUser().getId())) {
                    online.setUser(user);
                    redis.set(key, online, redis.getExpire(key));
                }
            });
        });
    }

    @Override
    public void resetPwd(User user) {
        user.setPassword(MD5Utils.getMD5(user.getPassword()));
        user.setUsername(null);
        user.setUpdated(new Date());
        userMapper.updateById(user);
    }

    @Override
    public Page<User> getAuthorizeList(Integer pageNum, Integer pageSize, Integer roleId, User user) {
        return contain(pageNum, pageSize, roleId, user, true);
    }

    @Override
    public Page<User> getUnAuthorizeList(Integer pageNum, Integer pageSize, Integer roleId, User user) {
        return contain(pageNum, pageSize, roleId, user, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userModify(User user, HttpServletRequest request) {
        if (user == null) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        String token = request.getHeader(ibaiq.getTokenHeader()).replace(ibaiq.getPrefix(), "");
        String username = tokenUtils.getUsername(token);
        Integer id = tokenUtils.getId(token);
        if (!username.equals(user.getUsername())) {
            throw new BaseException(MessageEnum.USER_ILLEGAL_OPERATION);
        }
        userMapper.updateById(infoModify(user, false));
        async.user_updateCache(user.getId());
    }

    @Override
    public void changePwd(String oldPwd, String newPwd, HttpServletRequest request) {
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        oldPwd = MD5Utils.getMD5(oldPwd);
        String token = request.getHeader(ibaiq.getTokenHeader()).replace(ibaiq.getPrefix(), "");
        Integer userId = tokenUtils.getId(token);
        User user = userMapper.selectById(userId);
        if (!oldPwd.equals(user.getPassword())) {
            throw new BaseException(MessageEnum.USER_OLD_PWD_INCONSISTENT);
        }
        user.setPassword(newPwd);
        userMapper.updateById(infoModify(user, true));
    }

    @Override
    public void updateCache(Integer userId) {
        User user = userMapper.selectById(userId);
        SpringUtils.getBean(RoleUserService.class).updateCache(user, null);
    }

    private Page<User> contain(Integer pageNum, Integer pageSize, Integer roleId, User user, boolean contain) {
        List<Integer> userIds;
        if (roleId != null) {
            userIds = roleUserMapper.selectUserIdList(roleId);
        } else {
            userIds = roleUserMapper.selectUserIds();
        }
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        if (contain) {
            query.in(User::getId, userIds);
        } else {
            query.notIn(User::getId, userIds);
        }
        query.like(StringUtils.isNotEmpty(user.getUsername()), User::getUsername, user.getUsername());
        query.orderByAsc(User::getId);
        Page<User> page = new Page<>(pageNum, pageSize);
        return userMapper.selectPage(page, query);
    }

    private User infoModify(User user, boolean changePwd) {
        if (changePwd) {
            user.setPassword(MD5Utils.getMD5(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        user.setUsername(null);
        user.setUpdated(new Date());
        user.setRoles(null);
        user.setCreated(null);
        user.setStatus(null);
        user.setDeleted(null);
        user.setRoleIds(null);
        return user;
    }

}
