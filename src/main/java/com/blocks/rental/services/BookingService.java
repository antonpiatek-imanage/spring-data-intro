package com.blocks.rental.services;

import com.blocks.rental.dtos.BookingDto;
import com.blocks.rental.dtos.CustomerDto;
import com.blocks.rental.entities.Booking;
import com.blocks.rental.entities.Car;
import com.blocks.rental.entities.Customer;
import com.blocks.rental.entities.Location;
import com.blocks.rental.mapper.BookingMapper;
import com.blocks.rental.pages.RentalPageable;
import com.blocks.rental.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class BookingService {




    @Autowired
    BookingRepository bookingRepository;



    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    private void save(Booking booking){
        bookingRepository.save(booking);
    }

    public Booking delete(long id){
        Booking booking = findById(id);
        bookingRepository.deleteById(id);
        return booking;
    }

    public Booking edit(long id, Car car, Location location,Customer customer, Date startDate, Date endDate) {
        Booking booking = findById(id);

        Date baseDate = null;

        try {
            baseDate = new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-02");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startDate.after(baseDate) &&
                ((endDate.after(startDate) && endDate.after(baseDate)) ||
                        (endDate.before(baseDate) && booking.endDate.after(startDate)))){
            booking.startDate = startDate;
        }
        if (endDate.after(baseDate) &&
                ((endDate.after(startDate) && startDate.after(baseDate)) ||
                        (startDate.before(baseDate) && booking.startDate.before(endDate)))){
            booking.endDate = endDate;
        }

        if (car != null){
            booking.car_link = car;

        }
        if (customer!=null){
            booking.customer_link = customer;
        }
        if(location != null){
            booking.location_link = location;
        }


        this.save(booking);
        return booking;
    }

    public Booking add(Date startDate, Date endDate, Car car_link, Location location_link, Customer customer_link) {
        Booking booking = new Booking(startDate,endDate, car_link, location_link, customer_link);
        this.save(booking);
        return booking;
    }

    public BookingDto mapToDto(Booking booking) {
        return BookingMapper.INSTANCE.bookingToBookingDto(booking);
    }

    public ArrayList<BookingDto> findPageToOutput(int page, int size, String sort, boolean ascending) {
        RentalPageable pageable = new RentalPageable(page, size, sort, ascending);
        Page pageReturned = bookingRepository.findAll(pageable);
        Stream<Booking> stream = pageReturned.get();
        ArrayList<BookingDto> arrayList = new ArrayList<>();
        stream.forEach(location -> arrayList.add(mapToDto(location)));

        return arrayList;    }
}
