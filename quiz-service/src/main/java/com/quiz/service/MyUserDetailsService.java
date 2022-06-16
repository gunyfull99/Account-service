//package com.quiz.service;
//
//import com.quiz.Dto.AccountDto;
//import com.quiz.Dto.BaseResponse;
//import com.quiz.exception.ResourceBadRequestException;
//import com.quiz.restTemplate.RestTemplateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Component
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private RestTemplateService restTemplateService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username ) throws ResourceBadRequestException {
//        AccountDto a = restTemplateService.getDetailUser(username);
//        if (a == null) {
//
//            throw new ResourceBadRequestException(new BaseResponse(400,"Không tìm thấy user"));
//        } else {
//
//            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//            a.getRoles().forEach(role -> {
//                authorities.add(new SimpleGrantedAuthority(role.getName()));
//            });
//            return new org.springframework.security.core.userdetails.User(a.getUsername(),"",
//                    authorities);
//        }
//    }
//
//
//
//
//}
