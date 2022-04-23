package com.account.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles_permissions")
public class RolePermission {
    @Id
    @SequenceGenerator(name = "rolePer_generator", sequenceName = "rolePer_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolePer_generator")
    private long id;
    private long roles_id;
    private  long permissions_id;
    private boolean can_read;
    private boolean can_update;
    private boolean can_create;
}
