package com.blocks.rental.repositories;
import com.blocks.rental.entities.Location;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LocationRepository extends PagingAndSortingRepository<Location, Long> {

}
