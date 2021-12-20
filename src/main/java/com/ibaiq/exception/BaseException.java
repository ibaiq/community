package com.ibaiq.exception;

import com.ibaiq.common.enums.MessageEnum;

import java.io.Serial;

/**
 * 基本异常
 *
 * @author 十三
 */
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1354377238168197802L;

    private Integer code;

    public BaseException() {

    }


    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(MessageEnum msg) {
        super(msg.getMsg());
        this.code = msg.getCode();
    }

    public BaseException(String msg, Throwable throwable) {
        super(msg, throwable);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
