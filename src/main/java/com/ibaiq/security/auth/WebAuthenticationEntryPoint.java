package com.ibaiq.security.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证验证入口
 *
 * @author 十三
 */
@Slf4j
@Component
public class WebAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 未认证的Security流程会走到这里
     * 然后将未登录的JSON响应给前端
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, request.getAttribute("msg") == null ? "请先登录" : request.getAttribute("msg").toString());
    }

}
