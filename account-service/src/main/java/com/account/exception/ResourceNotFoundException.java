package com.account.exception;

import com.account.Dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public ResourceNotFoundException(BaseResponse baseResponse) {
        super(baseResponse.getCode()+"@"+baseResponse.getMessage());

    }

}
