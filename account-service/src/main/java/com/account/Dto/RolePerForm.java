package com.account.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePerForm {
    private long roles_id;
    private  long permissions_id;
    private String can_read;
    private String can_update;
    private String can_create;
    private String name;
}
