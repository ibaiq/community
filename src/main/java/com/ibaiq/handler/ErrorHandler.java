package com.ibaiq.handler;

import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.entity.Message;
import com.ibaiq.exception.*;
import com.ibaiq.utils.I18nUtils;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletResponse;
import java.io.Serial;
import java.io.Serializable;

/**
 * 自定义异常处理器
 *
 * @author 十三
 */
@RestControllerAdvice
public class ErrorHandler implements Serializable {

    @Serial
    private static final long serialVersionUID = 1814778258832764613L;

    @ExceptionHandler(UserNotExistException.class)
    public Message handlerException(UserNotExistException e, HttpServletResponse response) {
        response.setStatus(e.getCode());
        return Message.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    public Message userExist(UserExistException e, HttpServletResponse response) {
        response.setStatus(e.getCode());
        return Message.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public Message handlerToken(TokenException e, HttpServletResponse response) {
        response.setStatus(e.getCode());
        return Message.error(e.getCode(), e.getMessage());
    }

    // @ExceptionHandler(BindException.class)
    // public String handleMethodArgumentNotValidException(BindException e, HttpServletRequest request) {
    //     int code = 500;
    //     request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, code);
    //     request.setAttribute("msg", Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    //
    //     return "forward:/error";
    // }

    // @ExceptionHandler(ConstraintViolationException.class)
    // public String returnMsg(ConstraintViolationException e, HttpServletRequest request) {
    //     int code = 500;
    //     String[] split = e.getMessage().split(":\\s");
    //     String msg = split[1];
    //     request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, code);
    //     request.setAttribute("msg", msg);
    //
    //     return "forward:/error";
    // }

    @ExceptionHandler(NumberFormatException.class)
    public Message isNumber(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return Message.error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("other.error_message.param_type"));
    }

    @ExceptionHandler(MultipartException.class)
    public Message fileIsNull() {
        return Message.error(HttpServletResponse.SC_BAD_REQUEST, I18nUtils.getMessage("other.error_message.file_is_null"));
    }

    @ExceptionHandler(BaseException.class)
    public Message base(BaseException e, HttpServletResponse response) {
        response.setStatus(e.getCode());
        return Message.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ParamIsNullException.class)
    public Message paramIsNull(ParamIsNullException e, HttpServletResponse response) {
        response.setStatus(e.getCode());
        return Message.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public Message paramIsNull(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return Message.error(MessageEnum.PARAM_IS_NULL);
    }

}
