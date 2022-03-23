package com.ibaiq.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.constants.UserConstants;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.entity.Menu;
import com.ibaiq.entity.User;
import com.ibaiq.entity.vo.MetaVo;
import com.ibaiq.entity.vo.RouterVo;
import com.ibaiq.exception.BaseException;
import com.ibaiq.exception.ParamIsNullException;
import com.ibaiq.mapper.MenuMapper;
import com.ibaiq.service.BaseService;
import com.ibaiq.service.MenuService;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.spring.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * （Menu）表业务实现类
 *
 * @author 十三
 */
@Slf4j
@Service
public class MenuServiceImpl extends BaseService<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getCurrentUserNav() {
        LambdaQueryWrapper<Menu> query = new LambdaQueryWrapper<>();
        User user = SecurityUtils.getUser().getUser();

        List<Menu> menus;
        if (user.isSysAdmin()) {
            menus = menuMapper.selectList(query.eq(Menu::getDeleted, 0)
                              .eq(Menu::getStatus, true)
                              .ne(Menu::getType, UserConstants.TYPE_BUTTON)
                              .orderByAsc(Menu::getSortNum));
        } else {
            List<Integer> roleIds = roleUserMapper.selectRoleIdList(user.getId());
            List<Integer> menuIds = roleMenuMapper.selectMenuIdsByRoleIds(roleIds);
            menus = menuMapper.selectList(query.in(Menu::getId, menuIds)
                              .eq(Menu::getDeleted, 0)
                              .eq(Menu::getStatus, true)
                              .ne(Menu::getType, UserConstants.TYPE_BUTTON)
                              .orderByAsc(Menu::getSortNum));
        }
        return getAllMenu(menus);
    }

    @Override
    public List<Menu> getAll(Menu menu) {
        LambdaQueryWrapper<Menu> query = new LambdaQueryWrapper<>();

        query.like(StringUtils.isNotEmpty(menu.getPerms()), Menu::getPerms, menu.getPerms())
                          .like(StringUtils.isNotEmpty(menu.getTitle()), Menu::getTitle, menu.getTitle())
                          // .eq(!SecurityUtils.getUser().getUser().isSysAdmin(), Menu::getDeleted, 0)
                          .eq(Menu::getDeleted, 0)
                          .eq(StringUtils.isNotNull(menu.getStatus()), Menu::getStatus, menu.getStatus())
                          .orderByAsc(Menu::getSortNum);

        List<Menu> menus = menuMapper.selectList(query);
        return getAllMenu(menus);
    }

    @Override
    public Menu findById(Integer id) {
        query.clear();
        if (id == null) {
            return null;
        }
        Menu menu = menuMapper.selectOne(query.eq(Menu::getDeleted, 0).eq(Menu::getId, id));
        if (menu == null) {
            throw new BaseException(MessageEnum.MENU_NOT_EXISTS);
        }
        return menu;
    }

    @Override
    public void add(Menu menu) {
        query.clear();
        query.eq(Menu::getPerms, menu.getPerms());
        Long count = menuMapper.selectCount(query);
        if (count > 0) {
            throw new BaseException(MessageEnum.MENU_EXISTS);
        }
        if (StringUtils.isNotNull(menu.getName())) {
            query.clear();
            query.eq(Menu::getName, menu.getName());
            Long selectCount = menuMapper.selectCount(query);
            if (selectCount > 0) {
                menu.setName(UUID.nameUUIDFromBytes(menu.getName().getBytes()).toString());
            }
        }
        menuMapper.insert(menu);
    }

    @Override
    @SneakyThrows
    public void deleteById(Integer id) {
        CountDownLatch latch = new CountDownLatch(2);
        query.clear();
        update.clear();

        CompletableFuture<Menu> menuGetMenu = async.menu_getMenu(id, latch);
        Menu menu = menuGetMenu.get();
        if (menu.isSystem()) {
            if (!SecurityUtils.getUser().getUser().isSysAdmin()) {
                throw new BaseException(MessageEnum.MENU_IS_SYSTEM_ROUTER);
            }
        }

        CompletableFuture<Long> menuGetCountByParentId = async.menu_getCountByParentId(id, latch);
        Long count = menuGetCountByParentId.get();
        latch.await();
        if (count > 0) {
            throw new BaseException(MessageEnum.NOT_CAN_DELETE);
        }

        menuMapper.update(null, update.eq(Menu::getId, id).set(Menu::getDeleted, 1));
        // 删除菜单后清除缓存
        clearUserAuthorityInfoByMenuId(id);
        // 同步删除中间关联表中
        async.menu_delRoleMenu(roleMenuMapper, id);
    }

    @Override
    public void modify(Menu menu) {
        query.clear();
        if (menu == null) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        if (menu.getId() == null) {
            throw new ParamIsNullException(MessageEnum.MENU_ID_IS_NULL);
        }
        Menu name_res;
        if (StringUtils.isNotEmpty(menu.getName())) {
            name_res = menuMapper.selectOne(query.eq(Menu::getName, menu.getName()));
            if (StringUtils.isNotNull(name_res) && !menu.getId().equals(name_res.getId())) {
                menu.setName(UUID.nameUUIDFromBytes(menu.getName().getBytes()).toString());
            }
            query.clear();
        }
        Menu id_res = menuMapper.selectOne(query.eq(Menu::getId, menu.getId()));
        if (!SecurityUtils.getUser().getUser().isSysAdmin()) {
            if (id_res.isSystem()) {
                menu.setPath(null);
            }
        }
        menu.setUpdated(new Date());
        menuMapper.updateById(menu);
        // 更新后清除权限缓存，会重新获取权限
        clearUserAuthorityInfoByMenuId(menu.getId());
        // AsyncManager.me().execute(AsyncFactory.asyncInvokeMethod("clearUserAuthorityInfoByMenuId", this.getClass(), menu.getId()));
    }

    @Override
    public List<RouterVo> buildRouter(List<Menu> menus) {
        List<RouterVo> routers = new ArrayList<>();
        menus.forEach(menu -> {
            RouterVo router = new RouterVo();
            router.setName(getRouterName(menu));
            router.setHidden(menu.getVisible());
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            // router.setMeta(new MetaVo(menu.getTitle(), menu.getIcon(), menu.getPath(), menu.getTitleEnUs(), menu.getTitleZhTw()));
            router.setMeta(new MetaVo(menu.getTitle(), menu.getIcon(), menu.getPath()));
            List<Menu> children = menu.getChildren();
            if (children != null && !children.isEmpty() && UserConstants.TYPE_DIR.equals(menu.getType())) {
                router.setAlwaysShow(true);
                router.setChildren(buildRouter(children));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo child = new RouterVo();
                child.setPath(getRouterPath(menu));
                child.setComponent(getComponent(menu));
                child.setName(getRouterName(menu));
                // child.setMeta(new MetaVo(menu.getTitle(), menu.getIcon(), menu.getPath(), menu.getTitleEnUs(), menu.getTitleZhTw()));
                child.setMeta(new MetaVo(menu.getTitle(), menu.getIcon(), menu.getPath()));
                childrenList.add(child);
                router.setChildren(childrenList);
            } else if (menu.getParentId() == 0 && isInnerLink(menu)) {
                router.setMeta(null);
                router.setPath("/inner");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo child = new RouterVo();
                String routerPath = StringUtils.replaceEach(menu.getPath(), new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
                child.setPath(routerPath);
                child.setComponent(UserConstants.INNER_LINK);
                child.setName(StringUtils.capitalize(routerPath));
                // child.setMeta(new MetaVo(menu.getTitle(), menu.getIcon(), menu.getPath(), menu.getTitleEnUs(), menu.getTitleZhTw()));
                child.setMeta(new MetaVo(menu.getTitle(), menu.getIcon(), menu.getPath()));
                childrenList.add(child);
                router.setChildren(childrenList);
            }
            routers.add(router);
        });
        return routers;
    }

    @Override
    public List<Menu> getDeleted() {
        query.clear();
        return menuMapper.selectList(query.eq(Menu::getDeleted, 1));
    }

    @Override
    public void recover(Integer menuId) {
        if (menuId == null) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        update.clear();

        menuMapper.update(null, update.eq(Menu::getId, menuId).set(Menu::getDeleted, 0));
    }


    private List<Menu> getAllMenu(List<Menu> menus) {
        List<Menu> list = new ArrayList<>();

        menus.forEach(menu -> {
            if (menu.getParentId() == 0) {
                list.add(menu);
            }
        });

        list.forEach(menu -> menu.setChildren(getChild(menu.getId(), menus)));

        return list;
    }

    /**
     * 递归子菜单
     *
     * @param id   父菜单id
     * @param data 子菜单数据
     * @return 递归集合
     */
    private List<Menu> getChild(Integer id, List<Menu> data) {
        List<Menu> childList = new ArrayList<>();

        data.forEach(menu -> {
            if (id.equals(menu.getParentId())) {
                childList.add(menu);
            }
        });

        childList.forEach(menu -> menu.setChildren(getChild(menu.getId(), data)));

        if (childList.isEmpty()) {
            return null;
        }

        return childList;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    private String getRouterName(Menu menu) {
        String routerName;
        if (StringUtils.isNotEmpty(menu.getName())) {
            routerName = StringUtils.capitalize(menu.getName());
        } else {
            routerName = StringUtils.capitalize(menu.getPath());
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    private String getRouterPath(Menu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId() != 0 && isInnerLink(menu)) {
            routerPath = StringUtils.replaceEach(routerPath, new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId() && UserConstants.TYPE_DIR.equals(menu.getType())) {
            routerPath = "/" + menu.getPath();
        } else if (isMenuFrame(menu)) {
            // 非外链并且是一级目录（类型为菜单）
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    private String getComponent(Menu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isMenuFrame(Menu menu) {
        return menu.getParentId() == 0 && UserConstants.TYPE_MENU.equals(menu.getType());
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isInnerLink(Menu menu) {
        return StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isParentView(Menu menu) {
        return menu.getParentId() != 0 && UserConstants.TYPE_DIR.equals(menu.getType());
    }

}
