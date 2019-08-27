package com.blocks.rental.mapper;

import com.blocks.rental.dtos.BookingDto;
import com.blocks.rental.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);
    @Mapping(source="car_link", target="car")
    @Mapping(source="customer_link", target = "customer")
    @Mapping(source="location_link", target="location")
    BookingDto bookingToBookingDto(Booking booking);
}
