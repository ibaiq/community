package com.ibaiq.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibaiq.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JSON响应数据
 *
 * @author 十三
 */
@Component
public class JsonAuthentication {

    @Autowired
    private ObjectMapper objectMapper;

    public void writerJson(HttpServletRequest request, HttpServletResponse response, Message data) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(data.getCode());
        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(data));
        writer.close();
    }

}
