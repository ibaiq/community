package com.ibaiq.security.auth;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.entity.LoginUser;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.User;
import com.ibaiq.entity.UserOnline;
import com.ibaiq.manager.AsyncManager;
import com.ibaiq.manager.factory.AsyncFactory;
import com.ibaiq.utils.I18nUtils;
import com.ibaiq.utils.TokenUtils;
import com.ibaiq.utils.ip.IpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * 登录成功处理器
 *
 * @author 十三
 */
@Slf4j
@Component
public class LoginSuccessHandler extends JsonAuthentication implements AuthenticationSuccessHandler {

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IbaiqConfig ibaiq;

    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        CountDownLatch latch = new CountDownLatch(1);
        // 获取当前ip
        String ip = IpUtils.getIpAddress(request);
        // 从验证结果中取出信息
        LoginUser principal = (LoginUser) auth.getPrincipal();
        // 生成UUID
        String uuid = UUID.randomUUID().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("id", principal.getId());
        map.put("username", principal.getUsername());
        map.put("uuid", uuid);
        // 将信息封装到token里面
        String token = tokenUtils.createToken(map);
        response.setHeader(ibaiq.getTokenHeader(), token);
        map.clear();

        UserOnline online = new UserOnline();
        online.setIp(ip);
        User user = principal.getUser();
        user.setPassword(null);
        online.setUser(user);
        online.setToken(token);
        AsyncManager.me().execute(AsyncFactory.asyncLoginLog(principal.getUsername(), Constants.LOGIN_SUCCESS, I18nUtils.getMessage(Constants.LOGIN_SUCCESS_MSG), uuid, online, latch));

        map.put("token", token);
        // 将JSON返回给前端
        latch.await();
        writerJson(request, response, Message.success(HttpServletResponse.SC_OK, I18nUtils.getMessage(Constants.LOGIN_SUCCESS_MSG), map));
    }

}
