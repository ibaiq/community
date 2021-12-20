package com.ibaiq.controller;

import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.converter.ProfileConverter;
import com.ibaiq.converter.UserConverter;
import com.ibaiq.service.*;
import com.ibaiq.utils.RedisUtils;
import com.ibaiq.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 父控制器
 *
 * @author 十三
 */
public abstract class BaseController {

    @Autowired
    protected RedisUtils redis;
    @Autowired
    protected TokenUtils tokenUtils;
    @Autowired
    protected IbaiqConfig ibaiq;


    @Autowired
    protected UserConverter userConverter;

    @Autowired
    protected ProfileConverter profileConverter;


    @Autowired
    protected UserService userService;

    @Autowired
    protected RoleService roleService;

    @Autowired
    protected UploadService uploadService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected RoleUserService roleUserService;

    @Autowired
    protected RoleMenuService roleMenuService;

    @Autowired
    protected LoginLogService logService;

    @Autowired
    protected SysConfigService sysConfigService;

    @Autowired
    protected OperLogService operLogService;


    protected HttpServletRequest request;

    protected HttpServletResponse response;

    @ModelAttribute
    protected void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

}
