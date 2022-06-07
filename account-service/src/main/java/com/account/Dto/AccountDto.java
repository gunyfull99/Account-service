package com.account.Dto;

import com.account.entity.Company;
import com.account.entity.Permission;
import com.account.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

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
    private long companyId;
    private Date birthDay;
    private Date startDay;
    private Set<Roles> roles = new HashSet<>();
    private Set<Permission> permissions = new HashSet<>();
}
