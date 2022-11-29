package com.marindulja.template.springresttemplate.service.customers;

import com.marindulja.template.springresttemplate.dto.CustomerDto;
import com.marindulja.template.springresttemplate.model.Customer;
import com.marindulja.template.springresttemplate.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private ModelMapper mapper = new ModelMapper();

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDto addCustomer(CustomerDto customerToBeAdded) {
        Customer customer = new Customer();
        customer.setFirstName(customerToBeAdded.getFirstName());
        customer.setLastName(customerToBeAdded.getLastName());
        customer.setAddress(customerToBeAdded.getAddress());
        Customer savedCustomer = customerRepository.save(customer);
        return mapToDTO(savedCustomer);
    }

    @Override
    public ResponseEntity<CustomerDto> getCustomerById(long id) {
        Optional<Customer> customerData = customerRepository.findById(id);
        if (customerData.isPresent()) {
            return new ResponseEntity<>(mapToDTO(customerData.get()), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("Customer was  not found");
        }
    }

    @Override
    public ResponseEntity<CustomerDto> updateCustomerById(long id, CustomerDto customerDto) {
        Optional<Customer> customerData = customerRepository.findById(id);
        if (customerData.isPresent()) {
            Customer customer = customerData.get();
            customer.setFirstName(customer.getFirstName());
            customer.setLastName(customer.getLastName());
            customer.setAddress(customer.getAddress());

            Customer updatedCustomer = customerRepository.save(customer);
            return new ResponseEntity<>(mapToDTO(updatedCustomer), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("Customer not found");
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteCustomerById(long id) {
        try {
            customerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("Customer was not found");
        }
    }

    private CustomerDto mapToDTO(Customer customer) {
        CustomerDto customerDto = mapper.map(customer, CustomerDto.class);
        return customerDto;
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        return customer;
    }
}
