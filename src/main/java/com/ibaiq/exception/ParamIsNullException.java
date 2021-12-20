package com.ibaiq.exception;

import com.ibaiq.common.enums.MessageEnum;

import java.io.Serial;

/**
 * 参数为空异常
 *
 * @author 十三
 */
public class ParamIsNullException extends BaseException {

    @Serial
    private static final long serialVersionUID = -8966200767428175823L;

    public ParamIsNullException() {
    }

    public ParamIsNullException(MessageEnum msg) {
        super(msg);
    }

}
