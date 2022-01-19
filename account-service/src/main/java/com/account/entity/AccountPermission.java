package com.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts_permissions")
public class AccountPermission {
    @Id
    @SequenceGenerator(name = "account_generator", sequenceName = "account_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    private long id;
    private long account_id;
    private  long permissions_id;
    private boolean canRead;
    private boolean canUpdate;
    private boolean canCreate;

}
