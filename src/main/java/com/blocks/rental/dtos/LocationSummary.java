package com.blocks.rental.dtos;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class LocationSummary{

    public long locationId;
    public long numberOfCars;
    public long totalBookings;
    public long activeBookings;
    public String location;


    public LocationSummary(long locationId, String location, long numberOfCars, long totalBookings, long activeBookings) {
        this.locationId = locationId;
        this.location = location;
        this.numberOfCars = numberOfCars;
        this.totalBookings = totalBookings;
        this.activeBookings = activeBookings;
    }
}
