package com.quiz.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private long id;
    private String username;
    private String fullName;
    private String email;
    private String userType;
    private String phone;
    private String address;
    private boolean isActive ;
    private CompanyDto company;
    private Set<RolesDto> roles = new HashSet<>();
    private Set<PermissionDto> permissions = new HashSet<>();
    private Date birthDay;
    private Date startDay;
}
