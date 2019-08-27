package com.blocks.rental.repositories;
import com.blocks.rental.entities.Car;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CarRepository extends PagingAndSortingRepository<Car, Long> {

}
