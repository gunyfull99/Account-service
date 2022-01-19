//package com.account.Dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
//import java.util.Collection;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class AccountDto implements UserDetails {
//
//
//    @NotEmpty
//    @Pattern(regexp = "^[a-zA-Z0-9]*$",message = "username must alpha numberic")
//    @Size(min = 6,max = 12,message = "username should between 6-12 characters")
//    private String username;
//
//    @NotEmpty
//    @Pattern(regexp = "^[a-zA-Z0-9]*$",message = "password must alpha numberic")
//    @Size(min = 8,max = 16,message = "password should between 8-16 characters")
//    private String password;
//    @NotEmpty
//    @Pattern(regexp = "^(active)|(deactive)$",message = "status  must active or deactive")
//    private String status;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
