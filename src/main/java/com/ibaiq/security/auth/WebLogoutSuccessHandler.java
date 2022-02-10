package com.ibaiq.security.auth;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.entity.Message;
import com.ibaiq.manager.AsyncManager;
import com.ibaiq.manager.factory.AsyncFactory;
import com.ibaiq.service.async.AsyncService;
import com.ibaiq.utils.I18nUtils;
import com.ibaiq.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销登录处理器
 *
 * @author 十三
 */
@Component
public class WebLogoutSuccessHandler extends JsonAuthentication implements LogoutSuccessHandler {

    @Autowired
    private AsyncService async;
    @Autowired
    private IbaiqConfig ibaiq;
    @Autowired
    private TokenUtils token;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            String token = request.getHeader(ibaiq.getTokenHeader()).replace(ibaiq.getPrefix(), "");
            Claims claims = this.token.claims(token);
            String username = claims.get("username", String.class);
            async.token_addBlacklist(claims.get("uuid", String.class));
            AsyncManager.me().execute(AsyncFactory.asyncLoginLog(username, Constants.LOGOUT_SUCCESS, Constants.LOGOUT_SUCCESS_MSG, null, null, null));
        } finally {
            writerJson(request, response, Message.success(I18nUtils.getMessage("logout.success_message")));
        }
    }

}
