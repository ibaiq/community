package com.ibaiq.controller.manage;

import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.Menu;
import com.ibaiq.entity.Message;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 菜单管理
 *
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE + "/menu")
public class MenuController extends BaseController {

    /**
     * 根据菜单编号获取菜单信息
     */
    @GetMapping({"/", "/{menuId}"})
    @PreAuthorize("@permission.hasPermits('sys:menu:query')")
    public Message findById(@PathVariable Integer menuId) {
        return Message.success(menuService.findById(menuId));
    }

    /**
     * 获取路由信息
     */
    @GetMapping("/getRouters")
    public Message getRouters() {
        List<Menu> nav = menuService.getCurrentUserNav();
        return Message.success(menuService.buildRouter(nav));
    }

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    @PreAuthorize("@permission.hasPermits('sys:menu:list')")
    public Message list(Menu menu) {
        return Message.success(menuService.getAll(menu));
    }

    /**
     * 获取父子关系菜单
     */
    @GetMapping("/nav")
    @PreAuthorize("@permission.hasPermits('sys:menu:list')")
    public Message nav() {
        return Message.success(menuService.getCurrentUserNav());
    }

    /**
     * 添加菜单
     */
    @PostMapping
    @PreAuthorize("@permission.hasPermits('sys:menu:save')")
    @Log(module = "菜单管理", businessType = BusinessType.INSERT)
    public Message addMenu(@RequestBody Menu menu) {
        menu.setCreated(new Date());
        menuService.add(menu);
        return Message.success();
    }

    /**
     * 修改菜单信息
     */
    @PutMapping
    @PreAuthorize("@permission.hasPermits('sys:menu:modify')")
    @Log(module = "菜单管理", businessType = BusinessType.UPDATE)
    public Message modify(@RequestBody Menu menu) {
        menu.setPerms(null);
        menuService.modify(menu);
        return Message.success();
    }

    /**
     * 删除菜单以及路由
     */
    @DeleteMapping("/{menuId}")
    @PreAuthorize("@permission.hasPermits('sys:menu:delete')")
    @Log(module = "菜单管理", businessType = BusinessType.DELETE)
    public Message delete(@PathVariable Integer menuId) {
        menuService.deleteById(menuId);
        return Message.success();
    }

    /**
     * 获取已删除的菜单
     */
    @GetMapping("/delMenu")
    @PreAuthorize("@permission.hasPermits('sys:menu:del:list')")
    public Message getDelete() {
        return Message.success(menuService.getDeleted());
    }

    /**
     * 恢复删除的菜单
     */
    @PostMapping("/recover/{menuId}")
    @PreAuthorize("@permission.hasPermits('sys:menu:del:recov')")
    @Log(module = "菜单管理", businessType = BusinessType.UPDATE)
    public Message recover(@PathVariable Integer menuId) {
        menuService.recover(menuId);
        return Message.success();
    }

}