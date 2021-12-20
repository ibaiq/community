package com.ibaiq.utils;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.entity.SysConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * token工具类
 *
 * @author 十三
 */
@Configuration
@SuppressWarnings("all")
public class TokenUtils {

    @Autowired
    private RedisUtils redis;
    @Autowired
    private IbaiqConfig ibaiq;


    /**
     * 生成token
     *
     * @param claims
     * @return token
     */
    public String createToken(Map<String, Object> claims) {
        Calendar instance = Calendar.getInstance();
        if (redis.hasKey(Constants.REDIS_PREFIX_LOGIN_EXPIRED)) {
            instance.add(Calendar.SECOND, Integer.parseInt(redis.get(Constants.REDIS_PREFIX_LOGIN_EXPIRED, SysConfig.class).getConfigValue()));
        } else {
            instance.add(Calendar.MINUTE, (int) ibaiq.getExpired().toMinutes());
        }
        return Jwts.builder()
                          .setClaims(claims)
                          .setIssuedAt(new Date())
                          .setExpiration(instance.getTime())
                          .signWith(SignatureAlgorithm.HS256, ibaiq.getSecret())
                          .compact();
    }

    /**
     * 解析token
     *
     * @param token token
     * @return 解析信息
     */
    public Claims claims(String token) {
        return Jwts.parser().setSigningKey(ibaiq.getSecret()).parseClaimsJws(token).getBody();
    }

    /**
     * 解析token中的用户名
     *
     * @param token
     * @return
     */
    public String getUsername(String token) {
        return claims(token).get("username", String.class);
    }

    /**
     * 解析token中的用户id
     *
     * @param token
     * @return
     */
    public Integer getId(String token) {
        return claims(token).get("id", Integer.class);
    }

    /**
     * token是否过期
     *
     * @param token token
     * @return
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = claims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
