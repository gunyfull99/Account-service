package com.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nominee")
public class Nominee {
    @Id
    @SequenceGenerator(name = "category_generator", sequenceName = "category_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
    private Long id;
    @Column
    private String name;

    private boolean status=true;

    @ManyToMany(mappedBy = "nomineeEntities")
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    private Collection<Category> categoryEntities;

}
