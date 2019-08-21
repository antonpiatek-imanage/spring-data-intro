package com.blocks.rental.repositories;
import org.springframework.data.repository.CrudRepository;
import com.blocks.rental.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{

}
