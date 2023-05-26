package com.marindulja.template.springresttemplate.service.customers;

import com.marindulja.template.springresttemplate.dto.CustomerDto;
import com.marindulja.template.springresttemplate.exception.NotFoundException;
import com.marindulja.template.springresttemplate.model.Category;
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

    /**
     * /**
     * Retrieves a paginated list of all customers.
     *
     * <p>This method retrieves a list of customers, but instead of returning all customers at once,
     * it returns them in a paginated manner to improve performance and usability. The size of each page can
     * be customized based on your needs.
     *
     * <p>Additionally, this method supports filtering, which allows you to retrieve a subset of customers based
     * on specific criteria. The exact filtering criteria that are supported will depend on your application's
     * requirements and how this method is implemented.
     *
     * @param pageRequest A {@code PageRequest} object that specifies the pagination information, such as the
     *                    number of the page and the size of each page.
     * @param searchValue A {@code Filter} object that specifies the filtering criteria. This may include fields
     *                    such as name, address, or any other fields that your application needs to filter by.
     * @return A {@code Page} object containing the paginated list of customers. If no customers exist, or if no
     * customers meet the filtering criteria, this method will return an empty {@code Page}.
     */
    @Override
    public Page<CustomerDto> getPaginatedAndFilteredCustomers(Pageable pageRequest, String searchValue) {
        Page<Customer> pageResult = customerRepository.findAll(pageRequest);
        List<CustomerDto> customersDto = pageResult
                .stream()
                .filter(res -> res.getFirstName().contains(searchValue) || res.getLastName().contains(searchValue))
                .filter(customer -> !customer.isDeleted())
                .map(this::mapToDTO)
                .collect(toList());
        return new PageImpl<>(customersDto, pageRequest, customersDto.size());
    }

    /**
     * Retrieves a list of all customers.
     */

    @Override
    public List<CustomerDto> getAllCustomers() {
        Iterable<Customer> categories = customerRepository.findAll();
        return StreamSupport.stream(categories.spliterator(), false)
                .filter(customer -> !customer.isDeleted())
                .map(this::mapToDTO)
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
            customer.setFirstName(customerDto.getFirstName());
            customer.setLastName(customerDto.getLastName());
            customer.setAddress(customerDto.getAddress());

            Customer updatedCustomer = customerRepository.save(customer);
            return new ResponseEntity<>(mapToDTO(updatedCustomer), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("Customer not found");
        }
    }

    @Override
    public void deleteCustomerById(long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    private CustomerDto mapToDTO(Customer customer) {
        return mapper.map(customer, CustomerDto.class);
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        return mapper.map(customerDto, Customer.class);
    }
}
