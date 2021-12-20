package com.ibaiq.security.auth;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.entity.Message;
import com.ibaiq.manager.AsyncManager;
import com.ibaiq.manager.factory.AsyncFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证失败处理器
 *
 * @author 十三
 */
@Component
public class LoginFailureHandler extends JsonAuthentication implements AuthenticationFailureHandler {

    /**
     * 认证失败后的操作
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        Message result;
        if (e instanceof BadCredentialsException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, "密码错误");
        } else if (e instanceof AccountExpiredException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, "账号已过期");
        } else if (e instanceof DisabledException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, "账号不可用");
        } else if (e instanceof InternalAuthenticationServiceException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, "用户不存在");
        } else if (e instanceof LockedException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, "账号已锁定，请联系管理员");
        } else {
            result = Message.error(500, "未知错误");
        }
        AsyncManager.me().execute(AsyncFactory.asyncLoginLog(request.getParameter("username"), Constants.LOGIN_FAIL, result.getMsg(), null, null, null));
        writerJson(request, response, result);
    }
}
