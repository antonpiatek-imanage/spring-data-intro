package com.blocks.rental.repositories;
import com.blocks.rental.entities.Booking;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {

}
