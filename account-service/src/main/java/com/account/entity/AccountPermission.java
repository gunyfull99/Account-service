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
    @SequenceGenerator(name = "accountPer_generator", sequenceName = "accountPer_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountPer_generator")
    private long id;
    private long account_id;
    private  long permissions_id;
    private String can_read;
    private String can_update;
    private String can_create;

}
