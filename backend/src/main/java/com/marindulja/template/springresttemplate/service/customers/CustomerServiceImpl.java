package com.marindulja.template.springresttemplate.service.customers;

import com.marindulja.template.springresttemplate.dto.CustomerDto;
import com.marindulja.template.springresttemplate.model.Customer;
import com.marindulja.template.springresttemplate.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Page<CustomerDto> getPaginatedAndFilteredCustomers(Pageable pageRequest, String searchValue)  {
        Page<Customer> pageResult = customerRepository.findAll(pageRequest);
        List<CustomerDto> customersDto = pageResult
                .stream()
                .filter(res-> res.getFirstName().contains(searchValue) || res.getLastName().contains(searchValue))
                .map(this::mapToDTO)
                .collect(toList());
        return new PageImpl<>(customersDto, pageRequest, customersDto.size());
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        Iterable<Customer> categories = customerRepository.findAll();
        return StreamSupport.stream(categories.spliterator(), false).map(this::mapToDTO)
                .collect(Collectors.toList());
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
        return mapper.map(customer, CustomerDto.class);
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        return mapper.map(customerDto, Customer.class);
    }
}
