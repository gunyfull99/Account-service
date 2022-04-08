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
    private boolean canRead;
    private boolean canUpdate;
    private boolean canCreate;
    private String name;
}
