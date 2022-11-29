package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
