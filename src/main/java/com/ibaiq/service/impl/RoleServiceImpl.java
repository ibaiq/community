package com.ibaiq.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.entity.Role;
import com.ibaiq.entity.RoleUser;
import com.ibaiq.entity.User;
import com.ibaiq.exception.BaseException;
import com.ibaiq.exception.ParamIsNullException;
import com.ibaiq.mapper.RoleMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.RoleService;
import com.ibaiq.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * （Role）表业务实现类
 *
 * @author 十三
 */
@Slf4j
@Service
public class RoleServiceImpl extends BaseService<RoleMapper, Role> implements RoleService {

    @Override
    public Role findById(Integer id) {
        if (id == null) {
            return null;
        }
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BaseException(MessageEnum.ROLE_NOT_EXISTS);
        }
        return role;
    }

    @Override
    public List<Integer> getMenuIdsByIds(Integer id) {
        if (id == null) {
            return null;
        }
        return roleMenuMapper.selectMenuIdsByRoleIds(ListUtil.toList(id));
    }

    @Override
    public List<Role> getAll() {
        return roleMapper.selectList(null);
    }

    @Override
    public void modify(Role role) {
        if (role == null) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        if (role.getId() == null) {
            throw new ParamIsNullException(MessageEnum.ROLE_ID_IS_NULL);
        }
        role.setUpdated(new Date());
        roleMapper.updateById(role);
        if (ObjectUtil.isNotNull(role.getStatus()) && !role.getStatus()) {
            LambdaQueryWrapper<RoleUser> query = new LambdaQueryWrapper<>();
            roleUserMapper.delete(query.eq(RoleUser::getRoleId, role.getId()));
        }
        getAuthRoleNames();
        clearUserAuthorityInfoByRoleId(role.getId());
    }

    @Override
    public void add(Role role) {
        query.clear();
        Long count = roleMapper.selectCount(query.eq(Role::getName, role.getName()));
        if (count > 0) {
            throw new BaseException(MessageEnum.ROLE_NAME_IS_EXIST);
        }
        role.setCreated(new Date());
        roleMapper.insert(role);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        if (ids.isEmpty()) {
            throw new ParamIsNullException(MessageEnum.ROLE_ID_IS_NULL);
        }

        List<Role> roles = roleMapper.selectBatchIds(ids);
        roles.forEach(role -> {
            if (role.getType()) {
                throw new BaseException(MessageEnum.ROLE_IS_SYS_IN);
            }
        });

        roleMapper.deleteBatchIds(ids);
        async.role_asyncDelRole(ids);
    }

    @Override
    public Page<Role> getPagination(Integer pageNum, Integer pageSize, Role role) {
        LambdaQueryWrapper<Role> query = new LambdaQueryWrapper<>();
        Page<Role> page = new Page<>(pageNum, pageSize);

        // 条件
        query.like(StringUtils.isNotEmpty(role.getName()), Role::getName, role.getName())
                          .like(StringUtils.isNotEmpty(role.getTitle()), Role::getTitle, role.getTitle())
                          .eq(StringUtils.isNotNull(role.getStatus()), Role::getStatus, role.getStatus())
                          .lt(StringUtils.isNotNull(role.getBeginTime()), Role::getCreated, role.getBeginTime())
                          .gt(StringUtils.isNotNull(role.getEndTime()), Role::getCreated, role.getEndTime());

        return roleMapper.selectPage(page, query);
    }

    @Override
    public Page<Role> getUnallocatedList(Integer pageNum, Integer pageSize) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        return roleMapper.selectUnallocatedList(page);
    }

    @Override
    public void asyncDelRole(List<Integer> ids) {
        clearUserAuthorityInfoByRoleIds(ids);
        roleUserMapper.batchDeleteByRoleIds(ids);

        // ids.forEach(id -> {
        // roleUserMapper.update(null, new LambdaUpdateWrapper<RoleUser>().set(RoleUser::getRoleId, 3).eq(RoleUser::getRoleId, id));
        // });
        // roleUserMapper.
    }

    @Override
    public User getUserAuthRole(Integer userId) {
        List<Integer> roleIds = roleUserMapper.selectRoleIdList(userId);
        User user = userMapper.selectById(userId);
        user.setRoleIds(roleIds);
        return user;
    }

    @Override
    @PostConstruct
    public List<String> getAuthRoleNames() {
        List<String> names = roleMapper.selectRoleNames();
        redis.set(Constants.REDIS_KEY_SECURITY_ROLES, names);
        return names;
    }

}
