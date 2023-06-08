package com.marindulja.orderapp.repository;

import com.marindulja.orderapp.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}
