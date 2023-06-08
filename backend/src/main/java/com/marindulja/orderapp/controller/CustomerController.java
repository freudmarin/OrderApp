package com.marindulja.orderapp.controller;

import com.marindulja.orderapp.dto.CustomerDto;
import com.marindulja.orderapp.service.customers.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("add")
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(customerService.addCustomer(customerDto), HttpStatus.OK);
    }
    @GetMapping()
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    @GetMapping("paginated")
    public Page<CustomerDto> getAllCustomersPaginated(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                      @RequestParam(name = "searchValue") String searchValue) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return customerService.getPaginatedAndFilteredCustomers(pageRequest, searchValue);
    }
    @GetMapping("{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") long id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") long id, @RequestBody CustomerDto customer) {
        return customerService.updateCustomerById(id, customer);
    }

    @DeleteMapping("{id}")
    public void  deleteCustomer(@PathVariable("id") long id) {
        customerService.deleteCustomerById(id);
    }
}

