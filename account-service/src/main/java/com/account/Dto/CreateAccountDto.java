package com.account.Dto;

import com.account.entity.Company;
import com.account.entity.Permission;
import com.account.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountDto {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String userType;
    private String phone;
    private String address;
    private boolean isActive ;
    private long company;
    private Date birthDay;
    private Date startDay;
    private long role ;
}
