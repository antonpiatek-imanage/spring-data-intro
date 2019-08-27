package com.blocks.rental.services;

import com.blocks.rental.dtos.CustomerDto;
import com.blocks.rental.dtos.LocationDto;
import com.blocks.rental.entities.Customer;
import com.blocks.rental.entities.Location;
import com.blocks.rental.mapper.CustomerMapper;
import com.blocks.rental.pages.RentalPageable;
import com.blocks.rental.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Stream;

@Service
public class CustomerService {


    @Autowired CustomerRepository customerRepository;

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    private void save(Customer customer){
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

    public CustomerDto mapToDto(Customer customer) {
        return CustomerMapper.INSTANCE.customerToCustomerDto(customer);
    }

    public ArrayList<CustomerDto> findPageToOutput(int page, int size, String sort, boolean ascending) {
        RentalPageable pageable = new RentalPageable(page, size, sort, ascending);
        Page pageReturned = customerRepository.findAll(pageable);
        Stream<Customer> stream = pageReturned.get();
        ArrayList<CustomerDto> arrayList = new ArrayList<>();
        stream.forEach(location -> arrayList.add(mapToDto(location)));

        return arrayList;
    }
}
