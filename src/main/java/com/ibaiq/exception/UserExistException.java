package com.ibaiq.exception;

import com.ibaiq.common.enums.MessageEnum;

import java.io.Serial;

/**
 * 用户已存在异常
 *
 * @author 十三
 */
public class UserExistException extends BaseException {

    @Serial
    private static final long serialVersionUID = 7324486236305180882L;

    public UserExistException() {

    }

    public UserExistException(String msg) {
        super(msg);
    }

    public UserExistException(MessageEnum msg) {
        super(msg);
    }

}
