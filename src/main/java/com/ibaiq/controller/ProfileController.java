package com.ibaiq.controller;

import cn.hutool.core.map.MapUtil;
import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.User;
import com.ibaiq.entity.vo.ProfileVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 个人信息
 *
 * @author 十三
 */
@RestController
@RequestMapping("/user/profile")
public class ProfileController extends BaseController {

    /**
     * 获取当前登录用户信息
     */
    @GetMapping
    public Message currentInfo() {
        ProfileVo profileVo = profileConverter.po2Vo(userService.getInfo());
        return Message.success(profileVo);
    }

    /**
     * 修改个人信息
     */
    @PutMapping
    @Log(module = "个人信息", businessType = BusinessType.UPDATE)
    public Message modifyInfo(@RequestBody User user, HttpServletRequest request) {
        userService.userModify(user, request);
        return Message.success();
    }

    /**
     * 上传头像
     */
    @PostMapping
    @Log(module = "个人信息", businessType = BusinessType.UPLOAD)
    public Message uploadAvatar(MultipartFile file) {
        String path = uploadService.uploadImage(file, ibaiq.getAvatar(), request);
        return Message.success(MapUtil.builder().put("avatar", path).map());
    }

    /**
     * 修改密码
     */
    @PutMapping("/changePwd")
    @Log(module = "个人信息", businessType = BusinessType.UPDATE)
    public Message changePwd(String oldPwd, String newPwd) {
        userService.changePwd(oldPwd, newPwd, request);
        return Message.success();
    }

}
