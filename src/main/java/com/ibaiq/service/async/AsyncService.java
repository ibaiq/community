package com.ibaiq.service.async;

import com.ibaiq.common.annotation.Log;
import com.ibaiq.entity.Menu;
import com.ibaiq.entity.OperLog;
import com.ibaiq.entity.User;
import com.ibaiq.mapper.RoleMenuMapper;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author 十三
 */
public interface AsyncService {

    void configInitial();

    /**
     * 更新token缓存中的信息
     */
    void user_updateCache(Integer userId);

    /**
     * 批量更新缓存
     */
    void user_batchUpdateCache(List<Integer> userIds);

    /**
     * 异步删除角色，刷新权限缓存，更换普通用户角色
     */
    void role_asyncDelRole(List<Integer> ids);

    /**
     * 异步更新在线用户的缓存信息
     */
    void user_updateCache(User user, String authority);

    /**
     * 异步记录操作日志
     */
    void log_asyncRecordLog(final JoinPoint point, final Exception e, Object result);

    /**
     * 异步获取IP地址和位置并设置
     */
    void log_setIpAndAddress(OperLog operLog, CountDownLatch latch, HttpServletRequest request);

    /**
     * 异步获取请求地址、参数和请求方式并设置
     */
    void log_setUrlAndMethodAndParam(JoinPoint point, OperLog operLog, Log log, CountDownLatch latch, HttpServletRequest request);

    /**
     * token加入黑名单
     */
    void token_addBlacklist(String uuid);

    /**
     * 同步删除中间关联表中
     */
    void menu_delRoleMenu(RoleMenuMapper roleMenuMapper, Integer menuId);

    /**
     * 异步获取菜单信息
     */
    CompletableFuture<Menu> menu_getMenu(Integer menuId, CountDownLatch latch);

    /**
     * 异步查询菜单数量
     */
    CompletableFuture<Long> menu_getCountByParentId(Integer parentId, CountDownLatch latch);

    /**
     * 测试
     */
    void test();

}
