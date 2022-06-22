package com.account.service;

import com.account.Dto.BaseResponse;
import com.account.entity.Account;
import com.account.exception.ResourceBadRequestException;
import com.account.exception.ResourceNotFoundException;
import com.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceBadRequestException {
        Account a = accountRepository.findByUsername(username);
        if (a == null) {

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy user"));
        } else {

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            a.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
//            a.getPermissions().forEach(permission -> {
//                authorities.add(new SimpleGrantedAuthority(permission.getName()));
//            });
            return new org.springframework.security.core.userdetails.User(a.getUsername(), a.getPassword(),
                    authorities);
        }
    }
//    User user = userRepository.getUserByUsername(username);
//
//        if (user == null) {
//        throw new UsernameNotFoundException("Could not find user");
//    }
//
//        return new MyUserDetails(user);

}
