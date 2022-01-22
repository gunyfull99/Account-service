package com.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company")
public class Company {


    @Id
    @SequenceGenerator(name="company_generator",sequenceName="company_seq")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="company_generator")
    private long id;
    private String name;

    private String email;
    private String phone;
    private String address;

   @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
   @JsonIgnore
    private Set<Account> account=new HashSet<>();
    private boolean isActive=true;

    public boolean getActive() {
        return this.isActive;
    }
}
