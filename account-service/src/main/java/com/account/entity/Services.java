package com.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "services")

public class Services {
    @Id
    @SequenceGenerator(name = "services_generator", sequenceName = "services_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "services_generator")
    private long id;

    private String code;
    private String name;
    private String description;

    @ManyToMany(fetch = EAGER)
    private Set<Company> companies = new HashSet<>();



}
