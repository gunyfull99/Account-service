package com.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {

    @Id
    @SequenceGenerator(name = "category_generator", sequenceName = "category_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
    private long id;

    private String name;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "Nominee_category", //Tạo ra một join Table tên là "address_person"
            joinColumns = @JoinColumn(name = "cate_id"),  // TRong đó, khóa ngoại chính là address_id trỏ tới class hiện tại (Address)
            inverseJoinColumns = @JoinColumn(name = "nominee_id") //Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Person)
    )
    private Collection<Nominee> nomineeEntities;
    private boolean isActive =true;
}
