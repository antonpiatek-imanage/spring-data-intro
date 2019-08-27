package com.blocks.rental.dtos;


import java.util.Date;

public class BookingDto {
    public long id;
    public CarDto car;
    public CustomerDto customer;
    public LocationDto location;
    public Date startDate;
    public Date endDate;

}
