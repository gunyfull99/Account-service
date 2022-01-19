package com.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//
//@ToString(exclude = "Account")
//@EqualsAndHashCode(exclude = "Account")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")

public class Account implements Serializable {
    @Id
    @SequenceGenerator(name = "account_generator", sequenceName = "account_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    private long id;

    @NotEmpty(message = "username must not empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username must alpha numberic")
    @Size(min = 6, max = 12, message = "username should between 6-12 characters")
    private String username;

    @NotEmpty(message = "password must not empty")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "password must alpha numberic")
//    @Size(min = 8,max = 16,message = "password should between 8-16 characters")
    private String password;

    @NotEmpty(message = "full name must not empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "full name must alpha numberic")
    @Size(min = 1, max = 50, message = "Full name should between 1-50 characters")
    private String fullName;
    private String email;
    private String address;
    private String userType;
    private boolean isActive =true;

    @ManyToOne
    //@JsonIgnore
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = EAGER)
    private Set<Roles> roles = new HashSet<>();

    @ManyToMany(fetch = EAGER)
    private Set<Permission> permissions = new HashSet<>();

    public boolean getActive() {
       return this.isActive;
    }


}
