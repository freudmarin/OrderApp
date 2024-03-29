package com.marindulja.orderapp.service.customers;

import com.marindulja.orderapp.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    Page<CustomerDto> getPaginatedAndFilteredCustomers(Pageable pageRequest, String searchValue);
    List<CustomerDto> getAllCustomers();
    CustomerDto addCustomer(CustomerDto customerToBeAdded);
    ResponseEntity<CustomerDto> getCustomerById(long id);
    ResponseEntity<CustomerDto> updateCustomerById(long id, CustomerDto customer);
    void  deleteCustomerById(long id);
}
