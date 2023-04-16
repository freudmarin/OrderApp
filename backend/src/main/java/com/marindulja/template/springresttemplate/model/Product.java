package com.marindulja.template.springresttemplate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Product extends BaseEntity<Long> {

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "description")
    private String description;
    @Column(name = "unit_price", nullable = false)
    private double unitPrice;
    @Column(name = "unit_in_stock", nullable = false)
    private Long  unitInStock;

    private Double discount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
