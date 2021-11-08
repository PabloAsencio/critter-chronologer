package com.udacity.jdnd.course3.critter.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Customer getOwnerByPet(Long petId) {
        return customerRepository.findCustomerByPetsId(petId);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
