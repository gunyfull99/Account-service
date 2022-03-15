package com.account.exception;

import com.account.Dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ResourceForbiddenRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public ResourceForbiddenRequestException(BaseResponse baseResponse) {
        super(baseResponse.getCode()+"@"+baseResponse.getMessage());
    }
}
