package com.ibaiq.entity;

import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.exception.BaseException;
import com.ibaiq.utils.I18nUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.Serial;
import java.io.Serializable;

/**
 * 响应数据实体类
 *
 * @author 十三
 */
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = -6097251089211202059L;

    private Integer code;

    private String msg;

    private Object data;


    public Message() {
    }

    public Message(int code) {
        this.code = code;
    }

    public Message(int code, String msg) {
        this(code);
        this.msg = msg;
    }

    public Message(int code, String msg, Object data) {
        this(code, msg);
        this.data = data;
    }


    public static Message success() {
        return new Message(HttpServletResponse.SC_OK, I18nUtils.getMessage("response.success_message"));
    }

    public static Message success(Integer code, String msg, Object data) {
        return new Message(code, msg, data);
    }

    public static Message success(String msg, Object data) {
        return new Message(HttpServletResponse.SC_OK, msg, data);
    }

    public static Message success(String msg) {
        return new Message(HttpServletResponse.SC_OK, msg);
    }

    public static Message success(Object data) {
        return new Message(HttpServletResponse.SC_OK, I18nUtils.getMessage("response.success_message"), data);
    }

    public static Message success(Integer code, String msg) {
        return new Message(code, msg);
    }

    public static Message error(BaseException e) {
        return error(e.getCode(), e.getMessage());
    }

    public static Message error(MessageEnum msg) {
        return error(msg.getCode(), msg.getMsg());
    }

    public static Message error(Integer code, String msg) {
        return new Message(code, msg);
    }

    public static Message error() {
        return error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("response.error_message"));
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
