package com.ibaiq.common.enums;

import com.ibaiq.utils.I18nUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 响应状态，消息枚举类
 *
 * @author 十三
 */
public enum MessageEnum {

    // 认证异常
    NOT_LOGIN(HttpServletResponse.SC_UNAUTHORIZED, I18nUtils.getMessage("login.not_login_message")),
    TOKEN_EXCEPTION(HttpServletResponse.SC_UNAUTHORIZED, I18nUtils.getMessage("login.error_message.token_exception")),
    LOGIN_EXPIRED(HttpServletResponse.SC_UNAUTHORIZED, I18nUtils.getMessage("login.error_message.login_expired")),
    PERMISSION_DENIED(HttpServletResponse.SC_FORBIDDEN, I18nUtils.getMessage("login.error_message.permission_denied")),

    //  其他异常
    PARAM_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("other.error_message.param_is_null")),
    UPLOAD_FILE_NULL(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("other.error_message.upload_file_null")),
    ILLEGAL_FORMAT(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("other.error_message.illegal_format")),
    NOT_CAN_DELETE(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("other.error_message.not_can_delete")),
    ERROR(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("other.error_message.error")),


    // Menu实体参数请求异常
    MENU_ID_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("menu.error_message.id_is_null")),
    TITLE_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("menu.error_message.title_is_null")),
    MENU_NOT_EXISTS(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("menu.error_message.not_exists")),
    MENU_EXISTS(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("menu.error_message.exists")),
    MENU_IS_SYSTEM_ROUTER(HttpServletResponse.SC_BAD_REQUEST, "menu.error_message.is_system_menu"),

    // 角色实体参数请求异常
    ROLE_ID_IS_NULL(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("role.error_message.id_is_null")),
    ROLE_NAME_IS_EXIST(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("role.error_message.name_is_exist")),
    ROLE_IS_SYS_IN(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("role.error_message.is_sys_in")),
    ROLE_NOT_EXISTS(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("role.error_message.not_exists")),

    // 系统配置参数业务异常
    CONFIG_IS_EXIST(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("sys_config.error_message.is_exists")),
    CONFIG_KEY_IS_BLANK(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("sys_config.error_message.key_is_blank")),

    // 用户业务异常
    USER_NOT_EXIST(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("user.error_message.not_exists")),
    USER_IS_EXIST(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("user.error_message.is_exists")),
    USER_ILLEGAL_OPERATION(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("user.error_message.illegal_operation")),
    USER_IS_ADMIN(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("user.error_message.is_admin")),
    USER_OLD_PWD_INCONSISTENT(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("user.error_message.old_pwd_inconsistent")),
    USER_NEW_OLD_PWD_CONSISTENT(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("user.error_message.new_old_consistent")),
    USER_IS_DELETE(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("user.error_message.is_delete"));


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
