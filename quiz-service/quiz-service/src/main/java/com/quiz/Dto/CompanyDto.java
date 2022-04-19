package com.quiz.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    @Id
    private long id;
    private String name;
    private String email;
    private String phone;
    private String shortCutName;
    private String address;
    private String logo;
    private Set<AccountDto> account=new HashSet<>();
    private boolean isActive;

}
