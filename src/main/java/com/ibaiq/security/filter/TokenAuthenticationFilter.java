package com.ibaiq.security.filter;

import com.alibaba.druid.util.StringUtils;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.entity.UserOnline;
import com.ibaiq.exception.BaseException;
import com.ibaiq.exception.TokenException;
import com.ibaiq.service.UserService;
import com.ibaiq.utils.RedisUtils;
import com.ibaiq.utils.TokenUtils;
import com.ibaiq.utils.ip.IpUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token认证过滤器
 *
 * @author 十三
 */
@SuppressWarnings("ALL")
@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IbaiqConfig ibaiq;
    @Autowired
    private RedisUtils redis;
    @Autowired
    private UserService userService;


    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(ibaiq.getTokenHeader());
        if (!StringUtils.isEmpty(token)) {
            // token不为空进行token验证解析
            UsernamePasswordAuthenticationToken authentication = getAuthentication(token.replace(ibaiq.getPrefix(), ""), request, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    @SneakyThrows
    private UsernamePasswordAuthenticationToken getAuthentication(String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            String ip = IpUtils.getIpAddress(request);
            // 解析token
            Claims claims = tokenUtils.claims(token);
            // 去除uuid
            String uuid = claims.get("uuid", String.class);
            // 判断redis中有没有该uuid的token信息
            if (redis.hasKey(Constants.REDIS_PREFIX_TOKEN + uuid)) {
                // 取出登录信息
                UserOnline online = redis.get(Constants.REDIS_PREFIX_TOKEN + uuid, UserOnline.class);
                // 判断缓存中token的IP、用户状态、是否被删除
                if (ip.equals(online.getIp()) && online.getUser().getStatus() && online.getUser().getDeleted().equals(0)) {
                    String authority = "";
                    // 判断缓存中有没有权限缓存
                    if (redis.hasKey(Constants.REDIS_PREFIX_AUTHORITY + claims.get("username", String.class))) {
                        authority = redis.get(Constants.REDIS_PREFIX_AUTHORITY + claims.get("username"), String.class);
                    } else {
                        // redis缓存中没有的话重新获取
                        authority = userService.getUserAuthorityInfo(online.getUser());
                    }
                    // 验证结果返回
                    return new UsernamePasswordAuthenticationToken(online, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authority));
                } else {
                    throw new BaseException(MessageEnum.TOKEN_EXCEPTION);
                }
            } else {
                throw new TokenException(MessageEnum.LOGIN_EXPIRED);
            }
        } catch (ExpiredJwtException e) {
            // 将token异常消息放到域中，给未认证调用
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, MessageEnum.LOGIN_EXPIRED.getCode());
            request.setAttribute(RequestDispatcher.ERROR_MESSAGE, MessageEnum.LOGIN_EXPIRED.getMsg());
            request.getRequestDispatcher("/error").forward(request, response);
        } catch (JwtException e) {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, MessageEnum.TOKEN_EXCEPTION.getCode());
            request.setAttribute(RequestDispatcher.ERROR_MESSAGE, MessageEnum.TOKEN_EXCEPTION.getMsg());
            request.getRequestDispatcher("/error").forward(request, response);
        } catch (BaseException e) {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, MessageEnum.TOKEN_EXCEPTION.getCode());
            request.setAttribute(RequestDispatcher.ERROR_MESSAGE, MessageEnum.TOKEN_EXCEPTION.getMsg());
            request.getRequestDispatcher("/error").forward(request, response);
        }
        // 如果有异常返回null
        return null;
    }

}
