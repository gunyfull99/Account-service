package com.account.config;

import org.springframework.stereotype.Component;

@Component
public class ResponseError {
     public static final int notFound = 80915;
    public static final int isExist = 80916;
    public static final int usernameEmpty = 80911;
    public static final int passwordEmpty = 80912;
    public static final int forbidden = 80913;
}
