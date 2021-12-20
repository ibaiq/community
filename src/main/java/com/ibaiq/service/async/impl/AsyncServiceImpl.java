package com.ibaiq.service.async.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.aspect.LogAspect;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.entity.*;
import com.ibaiq.mapper.MenuMapper;
import com.ibaiq.mapper.RoleMenuMapper;
import com.ibaiq.service.RoleService;
import com.ibaiq.service.RoleUserService;
import com.ibaiq.service.SysConfigService;
import com.ibaiq.service.UserService;
import com.ibaiq.service.async.AsyncService;
import com.ibaiq.utils.RedisUtils;
import com.ibaiq.utils.spring.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author 十三
 */
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private RedisUtils redis;


    @Override
    @Async("taskExecutor")
    public void configInitial() {
        SpringUtils.getBean(SysConfigService.class).init();
    }

    @Override
    @Async("taskExecutor")
    public void user_updateCache(Integer userId) {
        SpringUtils.getBean(UserService.class).updateCache(userId);
    }

    @Override
    @Async("taskExecutor")
    public void user_batchUpdateCache(List<Integer> userIds) {
        SpringUtils.getBean(UserService.class).batchUpdateCache(userIds);
    }

    @Override
    @Async("taskExecutor")
    public void role_asyncDelRole(List<Integer> ids) {
        SpringUtils.getBean(RoleService.class).asyncDelRole(ids);
    }

    @Override
    @Async("taskExecutor")
    public void user_updateCache(User user, String authority) {
        SpringUtils.getBean(RoleUserService.class).updateCache(user, authority);
    }

    @Override
    @Async("taskExecutor")
    public void log_asyncRecordLog(final JoinPoint point, final Exception e, Object result) {
        SpringUtils.getBean(LogAspect.class).handleLog(point, e, result);
    }

    @Override
    @Async("taskExecutor")
    public void log_setIpAndAddress(OperLog operLog, CountDownLatch latch, HttpServletRequest request) {
        SpringUtils.getBean(LogAspect.class).setIpAndAddress(operLog, latch, request);
    }

    @Override
    @Async("taskExecutor")
    public void log_setUrlAndMethodAndParam(JoinPoint point, OperLog operLog, Log log, CountDownLatch latch, HttpServletRequest request) {
        SpringUtils.getBean(LogAspect.class).setUrlAndMethodAndParam(point, operLog, log, latch, request);
    }

    @Override
    @Async("taskExecutor")
    public void token_addBlacklist(String uuid) {
        UserOnline online = redis.get(Constants.REDIS_PREFIX_TOKEN + uuid, UserOnline.class);
        redis.set(Constants.REDIS_PREFIX_TOKEN_BLACKLIST + uuid, online, redis.getExpire(Constants.REDIS_PREFIX_TOKEN + uuid));
        redis.del(Constants.REDIS_PREFIX_TOKEN + uuid);
    }

    @Override
    public void menu_delRoleMenu(RoleMenuMapper roleMenuMapper, Integer menuId) {
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId, menuId));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Menu> menu_getMenu(Integer menuId, CountDownLatch latch) {
        Menu menu = SpringUtils.getBean(MenuMapper.class).selectById(menuId);
        latch.countDown();
        return CompletableFuture.completedFuture(menu);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Long> menu_getCountByParentId(Integer parentId, CountDownLatch latch) {
        LambdaQueryWrapper<Menu> query = new LambdaQueryWrapper<>();
        MenuMapper mapper = SpringUtils.getBean(MenuMapper.class);
        Long count = mapper.selectCount(query.eq(Menu::getParentId, parentId));
        latch.countDown();
        return CompletableFuture.completedFuture(count);
    }


    @Override
    @SneakyThrows
    @Async("taskExecutor")
    public void test() {
        Thread.sleep(5000);
        log.info("睡着了");
    }

}
