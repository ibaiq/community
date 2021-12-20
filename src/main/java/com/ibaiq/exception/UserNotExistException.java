package com.ibaiq.exception;

import com.ibaiq.common.enums.MessageEnum;

import java.io.Serial;

/**
 * 用户不存在异常
 *
 * @author 十三
 */
public class UserNotExistException extends BaseException {

    @Serial
    private static final long serialVersionUID = -8834630448211722347L;

    public UserNotExistException(String msg) {
        super(msg);
    }

    public UserNotExistException(MessageEnum msg) {
        super(msg);
    }

    public UserNotExistException(String msg, Throwable throwable) {
        super(msg, throwable);
    }


}
