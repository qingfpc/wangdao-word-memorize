package com.cskaoyan.wordmemorize.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static cn.dev33.satoken.error.SaErrorCode.CODE_11012;
import static cn.dev33.satoken.error.SaErrorCode.CODE_11013;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public Result error(NotLoginException e) {
        e.printStackTrace();
        if (e.getCode() == CODE_11012) {
            return Result.build(null, ResultCodeEnum.TOKEN_INVALID);
        }

        if (e.getCode() == CODE_11013) {
            return Result.build(null, ResultCodeEnum.TOKEN_TIME_OUT);
        }
        return Result.build(null, ResultCodeEnum.LOGIN_AUTH);
    }

    @ExceptionHandler(SaTokenException.class)
    @ResponseBody
    public Result error(SaTokenException e) {
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.TOKEN_INVALID);

    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public Result error(NotPermissionException e) {
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.PERMISSION);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(ExcelParseException.class)
    @ResponseBody
    public Result exelError(ExcelParseException e) {
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result error(BusinessException e) {
        if (e.getCodeEnum() == null) {
            return Result.fail(e.getMessage());
        }
        return Result.build(null, e.getCodeEnum());
    }
}
