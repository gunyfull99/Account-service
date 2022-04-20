package com.account.Dto;

import com.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPaging {
    int total ;
    List<AccountDto>accounts_list;
    int page ;
    int limit ;
    String search;
    String role;
    String userType;


    public  AccountPaging(int totalElements,List<AccountDto> accountDtoList){
        this.total=totalElements;
        this.accounts_list=accountDtoList;
    }
}
