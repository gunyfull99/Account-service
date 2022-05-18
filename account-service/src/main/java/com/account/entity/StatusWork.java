package com.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "status_work")
public class StatusWork {
    @Id
    @SequenceGenerator(name = "status_work_generator", sequenceName = "status_work_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_work_generator")
    private long id;

    private String name;
}
