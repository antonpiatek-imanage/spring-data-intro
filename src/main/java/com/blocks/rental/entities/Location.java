package com.blocks.rental.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String country;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", fetch = FetchType.EAGER)
    private Set<Car> carsInLocation;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "location_link", fetch = FetchType.EAGER)
    private Set<Booking> bookings;

    public Location(String country){
        this.country = country;
    }

    public Location(){

    }



    @JsonIgnore
    public Set<Booking> getBookingsInLocation(){ return bookings; }

    @JsonIgnore
    public Set<Booking> getActiveBookingsInLocation(){
        Date today = new Date();
        return bookings.stream().filter(p ->p.endDate.after(today) && p.startDate.before(today)).
                collect(Collectors.toSet());
    }

    @JsonIgnore
    public Set<Car> getCarsInLocation(){
        return carsInLocation;
    }

    public long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }







}
