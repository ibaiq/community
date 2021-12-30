package com.ibaiq.common.constants;

/**
 * 常量池
 *
 * @author 十三
 */
public interface Constants {

    // 登录过期时间
    long LOGIN_EXPIRED = 60 * 60 * 24;

    String REDIS_PREFIX_LOGIN_EXPIRED = Constants.REDIS_PREFIX_CONFIG + "sys.login.expired";

    // redis过期时间
    long REDIS_EXPIRED = 60 * 60;

    // redis认证授权前缀
    String REDIS_PREFIX_AUTHORITY = "GrantedAuthority:";

    // redisToken前缀
    String REDIS_PREFIX_TOKEN = "Token:";

    // Token黑名单
    String REDIS_PREFIX_TOKEN_BLACKLIST = "BlackList-Token:";

    // redis登录用户信息前缀
    String REDIS_PREFIX_LOGIN = "LoginUser:";

    // redis角色持久层缓存前缀
    String REDIS_PREFIX_ROLE_LIST = "RoleList";

    String REDIS_PREFIX_CONFIG = "Sys-Config:";

    // security权限角色标识缓存key
    String REDIS_KEY_SECURITY_ROLES = "Security-Roles";

    // 前端访问日志key
    String REDIS_KEY_ACCESS_LOG = "Access-Log";

    // http请求
    String HTTP = "http://";

    // https请求
    String HTTPS = "https://";

    String GBK = "GBK";

    // 登录成功
    boolean LOGIN_SUCCESS = true;
    String LOGIN_SUCCESS_MSG = "登录成功";

    // 注销成功
    boolean LOGOUT_SUCCESS = true;
    String LOGOUT_SUCCESS_MSG = "注销登录";

    // 登录失败
    boolean LOGIN_FAIL = false;

    // 后台管理url前缀
    String MANAGE = "/manage";

    // 腾讯位置服务key
    String TENCENT_KEY = "GHIBZ-7CZKR-CZOWP-WVSNV-KSVGE-DKBRC";

    // 匿名访问用户
    String ANONYMOUS = "匿名用户";
}
