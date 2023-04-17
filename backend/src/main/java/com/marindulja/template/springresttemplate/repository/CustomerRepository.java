package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}
