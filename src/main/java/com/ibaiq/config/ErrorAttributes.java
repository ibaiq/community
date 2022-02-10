package com.ibaiq.config;

import com.ibaiq.utils.I18nUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义错误响应数据格式
 *
 * @author 十三
 */
@Component
public class ErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> result = super.getErrorAttributes(webRequest, options);

        Integer code = (Integer) result.get("status");
        Object msg = result.get("message");
        Map<String, Object> message = new HashMap<>();

        message.put("code", code);
        if (msg.toString().length() > 100) {
            message.put("msg", I18nUtils.getMessage("other.error_message.server_error"));
        } else {
            message.put("msg", "No message available".equals(msg) ? I18nUtils.getMessage("other.error_message.server_error") : msg);
        }
        message.put("data", null);

        return message;
    }

}
