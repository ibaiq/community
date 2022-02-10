package com.ibaiq.utils;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author 十三
 */
@Component
public class I18nUtils {

    private final static ResourceBundleMessageSource MESSAGE_SOURCE = new ResourceBundleMessageSource();

    static {
        MESSAGE_SOURCE.setBasename("i18n/messages");
        MESSAGE_SOURCE.setDefaultEncoding("UTF-8");
    }

    public static String getMessage(String key) {
        var header = ServletUtils.getRequest().getHeader("Accept-Language");
        var lang = "";
        if (ObjectUtil.isNotEmpty(header)) {
            lang = header.split(";")[0];
        }
        try {
            return MESSAGE_SOURCE.getMessage(key, null, new Locale(lang));
        } catch (NoSuchMessageException e) {
            return "";
        }
    }

}
