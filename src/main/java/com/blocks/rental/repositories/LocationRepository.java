package com.blocks.rental.repositories;
import org.springframework.data.repository.CrudRepository;
import com.blocks.rental.entities.Location;

public interface LocationRepository extends CrudRepository<Location, Long>{

}
