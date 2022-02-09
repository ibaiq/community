package com.ibaiq.controller.manage;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.RoleUser;
import com.ibaiq.entity.User;
import com.ibaiq.entity.request.UserAddRequest;
import com.ibaiq.entity.request.UserUpdateRequest;
import com.ibaiq.entity.vo.UserVo;
import com.ibaiq.exception.ParamIsNullException;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.encryption.MD5Utils;
import com.ibaiq.utils.spring.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台用户管理控制器
 *
 * @author 十三
 */
@Slf4j
@RestController
@RequestMapping(Constants.MANAGE + "/user")
public class UserController extends BaseController {

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping({"/", "/{userId}"})
    @PreAuthorize("hasAuthority('sys:user:query')")
    public Message find(@PathVariable(required = false) Integer userId) {
        UserVo userVo = userConverter.po2Vo(userService.findById(userId));
        return Message.success(userVo);
    }

    /**
     * 获取分页数据
     *
     * @param pageNum  页码
     * @param pageSize 数量
     */
    @GetMapping("/pagination")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Message getAll(@RequestParam(defaultValue = "1") String pageNum, @RequestParam(defaultValue = "10") String pageSize, User user) {
        return Message.success(userService.getAll(pageNum, pageSize, user, false));
    }

    /**
     * 获取一删除的分页数据
     */
    @GetMapping("/getDelUser")
    @PreAuthorize("hasAuthority('sys:user:del:list')")
    public Message getDelUser(@RequestParam(defaultValue = "1") String pageNum, @RequestParam(defaultValue = "5") String pageSize, User user) {
        return Message.success(userService.getAll(pageNum, pageSize, user, true));
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:save')")
    @Log(module = "用户管理", businessType = BusinessType.INSERT)
    public Message add(@RequestBody UserAddRequest user) {
        User po = userConverter.request2Po(user);
        po.setCreated(new Date());
        userService.add(po);
        return Message.success();
    }

    /**
     * 批量或者删除一个用户
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @Log(module = "用户管理", businessType = BusinessType.DELETE)
    public Message delete(@RequestBody List<Integer> userIds) {
        userService.delOrRestById(userIds, true);
        return Message.success();
    }

    /**
     * 批量或恢复一个删除的用户
     */
    @PostMapping("/recover")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @Log(module = "用户管理", businessType = BusinessType.UPDATE)
    public Message recoverDel(@RequestBody List<Integer> userIds) {
        userService.delOrRestById(userIds, false);
        return Message.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:modify')")
    @Log(module = "用户管理", businessType = BusinessType.UPDATE)
    public Message modify(@RequestBody UserUpdateRequest user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            user.setPassword(MD5Utils.getMD5(user.getPassword()));
        }
        User po = userConverter.request2Po(user);
        po.setUpdated(new Date());
        userService.modify(po);
        return Message.success();
    }

    /**
     * 修改用户状态
     *
     * @param status true正常，false禁用
     */
    @PutMapping("/{userId}/{status}")
    @PreAuthorize("hasAuthority('sys:user:modify')")
    @Log(module = "用户管理", businessType = BusinessType.UPDATE)
    public Message modifyStatus(@PathVariable Integer userId, @PathVariable Boolean status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        user.setUpdated(new Date());
        userService.modify(user);
        return Message.success();
    }

    /**
     * 修改用户头像
     */
    @PutMapping("/upAvatar/{userId}")
    @PreAuthorize("hasAuthority('sys:user:modify')")
    @Log(module = "用户管理", businessType = BusinessType.UPDATE)
    public Message modifyAvatar(@PathVariable Integer userId, MultipartFile file) {
        String url = uploadService.uploadImage(file, ibaiq.getAvatar(), request);
        userService.modifyAvatar(userId, url, request);
        return Message.success(MapUtil.builder().put("avatar", url).map());
    }

    /**
     * 授权角色
     */
    @PutMapping({"/authorize", "/authorize/{userId}"})
    @PreAuthorize("hasAuthority('sys:user:auth:role')")
    @Log(module = "用户管理", businessType = BusinessType.GRANT)
    public Message assignRole(@PathVariable Integer userId, @RequestBody List<Integer> roleIds) {
        if (ObjectUtil.isNull(userId) || ObjectUtil.isEmpty(roleIds)) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        if (SecurityUtils.getUser().getUser().isSysAdmin()) {
            if (userId.equals(1) && !roleIds.contains(1)) {
                roleIds.add(1);
            }
        } else {
            if (userId.equals(1)) {
                throw new ParamIsNullException(MessageEnum.USER_IS_ADMIN);
            }
        }
        List<RoleUser> roleUsers = new ArrayList<>();
        roleIds.forEach(roleId -> {
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleId(roleId);
            roleUsers.add(roleUser);
        });

        roleUserService.authorize(userId, roleUsers);
        return Message.success();
    }

    /**
     * 获取用户授权的角色
     */
    @GetMapping({"/authorize", "/authorize/{userId}"})
    @PreAuthorize("hasAuthority('sys:user:auth:role')")
    public Message getUserAuthRole(@PathVariable Integer userId) {
        if (StringUtils.isNull(userId)) {
            throw new ParamIsNullException(MessageEnum.PARAM_IS_NULL);
        }
        return Message.success(MapUtil.builder()
                          .put("roles", roleService.getAll())
                          .put("user", roleService.getUserAuthRole(userId))
                          .map());
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    @PreAuthorize("hasAuthority('sys:user:resetPwd')")
    @Log(module = "用户管理", businessType = BusinessType.UPDATE)
    public Message resetPwd(@RequestBody User user) {
        user.setPassword(sysConfigService.getKeyValue("sys.user.initPwd"));
        userService.resetPwd(user);
        return Message.success();
    }

}
