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
    int totalElements;
    List<AccountDto>accountDtoList;
    int offset;
    int pageSize;
    String search;

    public  AccountPaging(int totalElements,List<AccountDto> accountDtoList){
        this.totalElements=totalElements;
        this.accountDtoList=accountDtoList;
    }
}
