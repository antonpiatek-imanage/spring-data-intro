package com.blocks.rental.entities;

import com.example.demo.entities.ChildEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String country;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", fetch = FetchType.EAGER)
    Set<Car> carsInLocation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location_link", fetch = FetchType.EAGER)
    Set<Booking> bookings;

    public Location(String country){
        this.country = country;
    }
    public Location(){

    }




    public long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }
}
