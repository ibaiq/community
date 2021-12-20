package com.ibaiq.security.auth;

import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.entity.Message;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足处理器
 *
 * @author 十三
 */
@Component
public class WebAccessDeniedHandler extends JsonAuthentication implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        writerJson(request, response, Message.error(MessageEnum.PERMISSION_DENIED));
    }
}
