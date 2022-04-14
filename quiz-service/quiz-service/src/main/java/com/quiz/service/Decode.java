package com.quiz.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class Decode {

    public String getUsername(String token){
        DecodedJWT jwt = JWT.decode(token);
        return  jwt.getSubject();
    }
}
