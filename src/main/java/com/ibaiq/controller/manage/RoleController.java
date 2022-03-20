package com.ibaiq.controller.manage;

import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.Role;
import com.ibaiq.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 *
 * @author 十三
 */
@Slf4j
@RestController
@RequestMapping(Constants.MANAGE + "/role")
public class RoleController extends BaseController {

    /**
     * 获取指定编号角色信息
     */
    @GetMapping({"/", "/{roleId}"})
    @PreAuthorize("@permission.hasPermits('sys:role:query')")
    public Message info(@PathVariable(required = false) Integer roleId) {
        return Message.success(roleService.findById(roleId));
    }

    /**
     * 获取全部角色
     */
    @GetMapping("/list")
    @PreAuthorize("@permission.hasPermits('sys:role:list')")
    public Message list() {
        return Message.success(roleService.getAll());
    }

    /**
     * 获取角色信息分页数据
     */
    @GetMapping("/pagination")
    @PreAuthorize("@permission.hasPermits('sys:role:list')")
    public Message pagination(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, Role role) {
        return Message.success(roleService.getPagination(pageNum, pageSize, role));
    }

    /**
     * 获取未授权用户的角色
     */
    @GetMapping("/authorize/unallocatedList")
    @PreAuthorize("@permission.hasPermits('sys:role:unauth:user:role')")
    public Message unallocatedList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        return Message.success(roleService.getUnallocatedList(pageNum, pageSize));
    }

    /**
     * 获取角色授权的用户
     */
    @GetMapping({"/authorize/authorizeList", "/authorize/authorizeList/{roleId}"})
    @PreAuthorize("@permission.hasPermits('sys:role:auth:user')")
    public Message getAuthorizeList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize,
                                    @PathVariable(required = false) Integer roleId, User user) {
        return Message.success(userService.getAuthorizeList(pageNum, pageSize, roleId, user));
    }

    /**
     * 获取角色未授权的用户
     */
    @GetMapping({"/authorize/unAuthorizeList", "/authorize/unAuthorizeList/{roleId}"})
    @PreAuthorize("@permission.hasPermits('sys:role:unauth:user')")
    public Message getUnAuthorizeList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize,
                                      @PathVariable(required = false) Integer roleId, User user) {
        return Message.success(userService.getUnAuthorizeList(pageNum, pageSize, roleId, user));
    }

    /**
     * 获取角色id下的菜单id
     *
     * @param id 角色id
     */
    @GetMapping("/menuIdList/{id}")
    @PreAuthorize("@permission.hasPermits('sys:role:query:menu')")
    public Message getMenuIdByRoleId(@PathVariable Integer id) {
        return Message.success(roleService.getMenuIdsByIds(id));
    }

    /**
     * 添加角色信息
     */
    @PostMapping
    @PreAuthorize("@permission.hasPermits('sys:role:save')")
    @Log(module = "角色管理", businessType = BusinessType.INSERT)
    public Message add(@RequestBody Role role) {
        roleService.add(role);
        return Message.success();
    }

    /**
     * 修改角色信息
     *
     * @param role 信息
     */
    @PutMapping
    @PreAuthorize("@permission.hasPermits('sys:role:modify')")
    @Log(module = "角色管理", businessType = BusinessType.UPDATE)
    public Message modify(@RequestBody Role role) {
        roleService.modify(role);
        return Message.success();
    }

    /**
     * 批量删除角色
     *
     * @param roleId 角色id
     */
    @DeleteMapping
    @PreAuthorize("@permission.hasPermits('sys:role:delete')")
    @Log(module = "角色管理", businessType = BusinessType.DELETE)
    public Message delete(@RequestBody List<Integer> roleId) {
        roleService.deleteById(roleId);
        return Message.success();
    }

    /**
     * 批量选择授权用户
     */
    @PutMapping({"/authorize/select/", "/authorize/select/{roleId}"})
    @PreAuthorize("@permission.hasPermits('sys:role:auth:user')")
    @Log(module = "角色管理", businessType = BusinessType.GRANT)
    public Message selectAuthorize(@PathVariable Integer roleId, @RequestBody List<Integer> userIds) {
        roleUserService.selectAuthorize(roleId, userIds);
        return Message.success();
    }

    /**
     * 批量取消授权用户
     */
    @PutMapping({"/authorize/cancel/", "/authorize/cancel/{roleId}"})
    @PreAuthorize("@permission.hasPermits('sys:role:auth:user')")
    @Log(module = "角色管理", businessType = BusinessType.CANCEL)
    public Message cancelAuthorize(@PathVariable Integer roleId, @RequestBody List<Integer> userIds) {
        roleUserService.cancelAuthorize(roleId, userIds);
        return Message.success();
    }

    /**
     * 选择菜单
     */
    @PutMapping("/selectMenu/{roleId}")
    @PreAuthorize("@permission.hasPermits('sys:role:auth:menu')")
    @Log(module = "角色管理", businessType = BusinessType.GRANT)
    public Message selectMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleMenuService.selectMenu(roleId, menuIds);
        return Message.success();
    }

}
