package com.blocks.rental.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Car {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String name;
    public String registration;
    public String manufacturer;
    public String model;
    @Enumerated(EnumType.STRING)
    public Transmission transmission;
    @Enumerated(EnumType.STRING)
    public Category category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car_link", fetch = FetchType.EAGER)
    Set<Booking> bookings;


    @ManyToOne(optional = false)
    public Location location;


    public enum Transmission{
        none, manual, automatic
    }
    public enum Category{
        none, car, van, lorry, tractor, motorbike
    }

    public Car(String name, String registration, String manufacturer, String model, Transmission transmission,
               Category category, Location location){
        this.name = name;
        this.registration = registration;
        this.manufacturer = manufacturer;
        this.model = model;
        this.transmission = transmission;
        this.category = category;
        this.location = location;
    }

    @PreRemove
    private void removeFromSets(){
        location.getCarsInLocation().remove(this);

    }

    public Car(){

    }


}
