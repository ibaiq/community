package com.ibaiq.security.auth;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.entity.Message;
import com.ibaiq.manager.AsyncManager;
import com.ibaiq.manager.factory.AsyncFactory;
import com.ibaiq.utils.I18nUtils;
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
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("login.error_message.password"));
        } else if (e instanceof AccountExpiredException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("login.error_message.account_expire"));
        } else if (e instanceof DisabledException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("login.error_message.account_disable"));
        } else if (e instanceof InternalAuthenticationServiceException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("login.error_message.account_not_found"));
        } else if (e instanceof LockedException) {
            result = Message.error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("login.error_message.account_locked"));
        } else {
            result = Message.error(500, I18nUtils.getMessage("login.error_message.unknown"));
        }
        AsyncManager.me().execute(AsyncFactory.asyncLoginLog(request.getParameter("username"), Constants.LOGIN_FAIL, result.getMsg(), null, null, null));
        writerJson(request, response, result);
    }
}
