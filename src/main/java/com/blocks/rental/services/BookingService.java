package com.blocks.rental.services;

import com.blocks.rental.entities.Booking;
import com.blocks.rental.entities.Car;
import com.blocks.rental.entities.Customer;
import com.blocks.rental.entities.Location;
import com.blocks.rental.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.Date;

@Service
public class BookingService {




    @Autowired
    BookingRepository bookingRepository;



    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void save(Booking booking){
        bookingRepository.save(booking);
    }

    public Booking delete(long id){
        Booking booking = findById(id);
        bookingRepository.deleteById(id);
        return booking;
    }

    public Booking edit(long id, Date startDate, Date endDate) {
        Booking booking = findById(id);

        booking.startDate = startDate;
        booking.endDate = endDate;

        this.save(booking);
        return booking;
    }

    public Booking add(Date startDate, Date endDate, Car car_link, Location location_link, Customer customer_link) {
        Booking booking = new Booking(startDate,endDate, car_link, location_link, customer_link);
        this.save(booking);
        return booking;
    }
}
