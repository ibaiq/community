package com.ibaiq.common.enums;

import javax.servlet.http.HttpServletResponse;

/**
 * 响应状态，消息枚举类
 *
 * @author 十三
 */
public enum MessageEnum {

    // 认证异常
    NOT_LOGIN(HttpServletResponse.SC_UNAUTHORIZED, "请先登录"),
    TOKEN_EXCEPTION(HttpServletResponse.SC_UNAUTHORIZED, "Token无效"),
    LOGIN_EXPIRED(HttpServletResponse.SC_UNAUTHORIZED, "登录超时，请重新登录"),
    PERMISSION_DENIED(HttpServletResponse.SC_FORBIDDEN, "权限不足，请联系管理员"),

    //  其他异常
    PARAM_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, "缺少参数"),
    UPLOAD_FILE_NULL(HttpServletResponse.SC_BAD_REQUEST, "请选择文件"),
    ILLEGAL_FORMAT(HttpServletResponse.SC_BAD_REQUEST, "图片格式非法"),
    NOT_CAN_DELETE(HttpServletResponse.SC_BAD_REQUEST, "请先删除子菜单"),
    ERROR(HttpServletResponse.SC_BAD_REQUEST, "服务器异常~"),


    // Menu实体参数请求异常
    MENU_ID_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, "菜单id不能为空"),
    TITLE_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, "菜单名称不能为空"),
    MENU_NOT_EXISTS(HttpServletResponse.SC_BAD_REQUEST, "菜单不存在"),
    MENU_EXISTS(HttpServletResponse.SC_BAD_REQUEST, "菜单唯一权限已存在"),
    MENU_IS_SYSTEM_ROUTER(HttpServletResponse.SC_BAD_REQUEST, "系统菜单无法删除"),

    // 角色实体参数请求异常
    ROLE_ID_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, "角色id不能为空"),
    ROLE_NAME_IS_EXIST(HttpServletResponse.SC_BAD_REQUEST, "角色名称已存在"),
    ROLE_IS_SYS_IN(HttpServletResponse.SC_BAD_REQUEST, "系统内置角色无法删除"),
    ROLE_NOT_EXISTS(HttpServletResponse.SC_BAD_REQUEST, "角色不存在"),

    // 系统配置参数业务异常
    CONFIG_IS_EXIST(HttpServletResponse.SC_BAD_REQUEST, "配置唯一键已存在"),
    CONFIG_KEY_IS_BLANK(HttpServletResponse.SC_BAD_REQUEST, "配置唯一键不能为空"),

    // 用户业务异常
    USER_NOT_EXIST(HttpServletResponse.SC_BAD_REQUEST, "用户不存在"),
    USER_IS_EXIST(HttpServletResponse.SC_BAD_REQUEST, "用户已存在"),
    USER_ILLEGAL_OPERATION(HttpServletResponse.SC_BAD_REQUEST, "非法操作"),
    USER_IS_ADMIN(HttpServletResponse.SC_BAD_REQUEST, "无法操作管理员用户"),
    USER_OLD_PWD_INCONSISTENT(HttpServletResponse.SC_BAD_REQUEST, "旧密码不一致"),
    USER_IS_DELETE(HttpServletResponse.SC_BAD_REQUEST, "账号已被删除请联系管理员");


    private Integer code;

    private String msg;

    MessageEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
