package com.account.Dto;

import lombok.Data;

@Data
public class ChangePassForm {

    private String username;
    private String oldPass;
    private String newPass;
    private String reNewPass;

}
