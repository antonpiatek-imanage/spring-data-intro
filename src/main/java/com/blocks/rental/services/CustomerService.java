package com.blocks.rental.services;

import com.blocks.rental.entities.Customer;
import com.blocks.rental.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


    @Autowired
    CustomerRepository customerRepository;

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void save(Customer customer){
        customerRepository.save(customer);
    }

    public Customer delete(long id){
        Customer customer = findById(id);
        customerRepository.deleteById(id);
        return customer;
    }

    public Customer editCustomer(long id, String name) {
        Customer customer = findById(id);
        customer.name = name;
        this.save(customer);
        return customer;
    }

    public Customer add(String name) {
        Customer customer = new Customer(name);
        this.save(customer);
        return customer;
    }
}
