package com.marindulja.orderapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")

public class Customer extends BaseEntity<Long> {
    @Column(name="first_name")

    private String firstName;

    @Column(name="last_name")
    private String lastName;

    private String address;

    @Column(name= "is_deleted")
    private boolean isDeleted;
}
