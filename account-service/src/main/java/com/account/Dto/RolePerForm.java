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
    private boolean can_read;
    private boolean can_update;
    private boolean can_create;
    private String name;
}
