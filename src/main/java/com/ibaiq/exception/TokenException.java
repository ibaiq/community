package com.ibaiq.exception;

import com.ibaiq.common.enums.MessageEnum;

import java.io.Serial;

/**
 * token异常
 *
 * @author 十三
 */
public class TokenException extends BaseException {

    @Serial
    private static final long serialVersionUID = -8528330803827325301L;

    public TokenException(String msg) {
        super(msg);
    }

    public TokenException(MessageEnum msg) {
        super(msg);
    }

}
