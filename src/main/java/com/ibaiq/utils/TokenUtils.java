package com.ibaiq.utils;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.entity.SysConfig;
import com.ibaiq.entity.UserOnline;
import com.ibaiq.utils.ip.AddressUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;

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
     * 刷新token
     *
     * @return token
     */
    public String refreshToken(Claims claims, UserOnline online, String ip) {
        var instance = Calendar.getInstance();
        var refresh = claims.get("refresh", Integer.class);
        refresh = Optional.ofNullable(refresh).orElse(0);
        int day;
        switch (refresh) {
            case 0:
                day = 1;
                break;
            case 1:
                day = 3;
                break;
            case 2:
                day = 7;
                break;
            default:
                day = 15;
                break;
        }
        instance.add(Calendar.DATE, day);
        var uuid = UUID.randomUUID().toString();
        claims.replace("uuid", uuid);
        claims.put("refresh", refresh++);
        var token = Jwts.builder()
                          .setClaims(claims)
                          .setIssuedAt(new Date())
                          .setExpiration(instance.getTime())
                          .signWith(SignatureAlgorithm.HS256, ibaiq.getSecret())
                          .compact();

        redis.del(Constants.REDIS_PREFIX_TOKEN + claims.get("uuid", String.class));
        online.setUuid(uuid);
        online.setIp(ip);
        online.setToken(token);
        online.setLoginLocation(AddressUtils.getCityInfo(ip));
        redis.set(Constants.REDIS_PREFIX_TOKEN + uuid, online, (day * 60 * 60 * 24));
        return token;
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
