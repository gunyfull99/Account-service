package com.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(length = 100, nullable = false)
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z]+$",message = "Role must charecters")
    private String name;

    @ManyToMany(fetch = EAGER)
    private Set<Permission> permissions = new HashSet<>();

}
