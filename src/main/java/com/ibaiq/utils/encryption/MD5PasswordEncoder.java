package com.ibaiq.utils.encryption;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * MD5加密验证
 *
 * @author 十三
 */
public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return MD5Utils.getMD5(String.valueOf(charSequence));
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(MD5Utils.getMD5(String.valueOf(charSequence)));
    }
}
