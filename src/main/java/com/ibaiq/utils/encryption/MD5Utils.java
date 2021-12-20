package com.ibaiq.utils.encryption;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * MD5加密封装工具类
 *
 * @author 十三
 */
public class MD5Utils {

    public static String getMD5(String value) {
        return DigestUtils.md5DigestAsHex(value.getBytes(StandardCharsets.UTF_8));
    }

}
