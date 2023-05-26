package com.marindulja.template.springresttemplate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category  extends BaseEntity<Long> {

    private String name;

    @OneToMany(
            mappedBy = "category"
    )
    private List<Product> products = new ArrayList<>();

    @Column(name= "is_deleted")
    private boolean isDeleted;
}
