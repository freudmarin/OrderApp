package com.marindulja.template.springresttemplate.service.customers;

import com.marindulja.template.springresttemplate.dto.CustomerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();
    CustomerDto addCustomer(CustomerDto customerToBeAdded);
    ResponseEntity<CustomerDto> getCustomerById(long id);
    ResponseEntity<CustomerDto> updateCustomerById(long id, CustomerDto customer);
    ResponseEntity<HttpStatus> deleteCustomerById(long id);
}
