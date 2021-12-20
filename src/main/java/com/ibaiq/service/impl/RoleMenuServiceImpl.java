package com.ibaiq.service.impl;

import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.entity.RoleMenu;
import com.ibaiq.exception.ParamIsNullException;
import com.ibaiq.manager.AsyncManager;
import com.ibaiq.manager.factory.AsyncFactory;
import com.ibaiq.mapper.RoleMenuMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * （RoleMenu）表业务实现类
 *
 * @author 十三
 */
@Service
public class RoleMenuServiceImpl extends BaseService<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Integer roleId, List<RoleMenu> roleMenus) {
        query.clear();
        this.remove(query.eq(RoleMenu::getRoleId, roleId));
        this.saveBatch(roleMenus);

        AsyncManager.me().execute(AsyncFactory.asyncInvokeMethod("clearUserAuthorityInfoByRoleId", this.getClass(), roleId));
    }

    @Override
    public void selectMenu(Integer roleId, List<Integer> menuIds) {
        if (roleId == null || menuIds == null || menuIds.isEmpty()) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        query.clear();
        this.remove(query.eq(RoleMenu::getRoleId, roleId));
        List<RoleMenu> roleMenus = new ArrayList<>();
        menuIds.forEach(item -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(item);
            roleMenus.add(roleMenu);
        });

        this.saveBatch(roleMenus);
    }

}
