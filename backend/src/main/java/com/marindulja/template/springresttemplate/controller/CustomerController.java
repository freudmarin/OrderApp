package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.CustomerDto;
import com.marindulja.template.springresttemplate.service.customers.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(customerService.addCustomer(customerDto), HttpStatus.OK);
    }

    @GetMapping("/")
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") long id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") long id, @RequestBody CustomerDto customer) {
        return customerService.updateCustomerById(id, customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") long id) {
        return customerService.deleteCustomerById(id);
    }
}

