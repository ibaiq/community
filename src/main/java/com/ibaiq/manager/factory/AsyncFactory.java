package com.ibaiq.manager.factory;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ibaiq.common.Common;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.entity.*;
import com.ibaiq.mapper.RoleUserMapper;
import com.ibaiq.service.LoginLogService;
import com.ibaiq.service.PermissionService;
import com.ibaiq.service.impl.RoleServiceImpl;
import com.ibaiq.utils.RedisUtils;
import com.ibaiq.utils.ServletUtils;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.ip.AddressUtils;
import com.ibaiq.utils.ip.IpUtils;
import com.ibaiq.utils.spring.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * 异步工厂（产生任务用）
 *
 * @author 十三
 */
@Slf4j
public class AsyncFactory {

    /**
     * 登录成功异步缓存登录信息
     *
     * @param uuid   会话编号
     * @param online 缓存的内容
     */
    public static TimerTask asyncLoginLog(final String username, final boolean status, final String msg,
                                          final String uuid, final UserOnline online, final CountDownLatch latch) {

        final HttpServletRequest request = ServletUtils.getRequest();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddress(request);
        final IbaiqConfig ibaiq = SpringUtils.getBean(IbaiqConfig.class);
        final PermissionService permissionService = SpringUtils.getBean(PermissionService.class);
        final Common common = SpringUtils.getBean(Common.class);

        return new TimerTask() {
            @Override
            public void run() {

                if (status && StringUtils.isNotEmpty(uuid) && online != null) {
                    RedisUtils redis = SpringUtils.getBean(RedisUtils.class);
                    online.setUsername(username);
                    online.setUuid(uuid);
                    online.setOs(userAgent.getOperatingSystem().getName());
                    online.setBrowser(userAgent.getBrowser().getName());
                    online.setLoginTime(new Date());
                    online.setLoginLocation(AddressUtils.getCityInfo(online.getIp()));
                    User user = online.getUser();
                    user = common.setRoleIdsAndRoles(user);
                    user.setPermissions(permissionService.getMenuPermission(user));
                    online.setUser(user);
                    if (redis.hasKey(Constants.REDIS_PREFIX_LOGIN_EXPIRED)) {
                        redis.set(Constants.REDIS_PREFIX_TOKEN + uuid, online, Long.parseLong(redis.get(Constants.REDIS_PREFIX_LOGIN_EXPIRED, SysConfig.class).getConfigValue()));
                    } else {
                        redis.set(Constants.REDIS_PREFIX_TOKEN + uuid, online, ibaiq.getExpired().toSeconds());
                    }
                }
                if (latch != null) {
                    latch.countDown();
                }
                LoginLog loginLog = new LoginLog();
                loginLog.setUsername(username);
                loginLog.setIp(ip);
                loginLog.setStatus(status);
                loginLog.setLoginLocation(AddressUtils.getCityInfo(ip));
                loginLog.setBrowser(userAgent.getBrowser().getName());
                loginLog.setOs(userAgent.getOperatingSystem().getName());
                loginLog.setMsg(msg);
                loginLog.setLoginTime(new Date());

                SpringUtils.getBean(LoginLogService.class).recording(loginLog);
            }
        };
    }

    public static TimerTask asyncSetCache(final String key, final Object value, final long expired) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(RedisUtils.class).set(key, value, expired);
            }
        };
    }

    public static TimerTask asyncListCache(final String key, final List<Object> value, final long expired) {
        return new TimerTask() {
            @Override
            public void run() {
                RedisUtils redis = SpringUtils.getBean(RedisUtils.class);
                redis.del(key);
                redis.listSet(key, value, expired);
            }
        };
    }

    public static TimerTask asyncDelRoleAndUpRole(List<Integer> ids) {
        return new TimerTask() {
            @Override
            public void run() {
                RoleServiceImpl service = SpringUtils.getBean(RoleServiceImpl.class);
                RoleUserMapper roleUserMapper = SpringUtils.getBean(RoleUserMapper.class);
                ids.forEach(id -> {
                    service.clearUserAuthorityInfoByRoleId(id);
                    roleUserMapper.update(null, new LambdaUpdateWrapper<RoleUser>().set(RoleUser::getRoleId, 3).eq(RoleUser::getRoleId, id));
                });
            }
        };
    }

    public static TimerTask asyncInvokeMethod(String methodName, Class<?> beanClass, Object... parameter) {
        return new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                Object bean = SpringUtils.getBean(beanClass);
                Class<?>[] clzs = new Class[parameter.length];
                for (int i = 0; i < clzs.length; i++) {
                    clzs[i] = parameter[i].getClass();
                }
                Method method = bean.getClass().getMethod(methodName, clzs);
                method.invoke(bean, parameter);
            }
        };
    }

    public static TimerTask test() {
        return new TimerTask() {
            @Override
            @SneakyThrows
            public void run() {
                Thread.sleep(5000);
                log.info("睡着了");
            }
        };
    }



}
