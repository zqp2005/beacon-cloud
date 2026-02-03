package com.mashibing.api.advice;

import com.mashibing.api.util.R;
import com.mashibing.api.vo.ResultVo;
import com.mashibing.common.exception.ApiException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResultVo apiException(ApiException ex)
    {
        return R.error(ex);
    }
}
