package com.blocks.rental.repositories;
import com.blocks.rental.entities.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long>{

}
