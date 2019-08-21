package com.blocks.rental.entities;

import com.blocks.rental.controllers.RentalController;
import com.example.demo.entities.ParentEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    public String transmission;
    public String category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car_link", fetch = FetchType.EAGER)
    Set<Booking> bookings;


    @ManyToOne(optional = false)
    public Location location;

    public Car(String name, String registration, String manufacturer, String model, String transmission, String category,
               Location location){
        this.name = name;
        this.registration = registration;
        this.manufacturer = manufacturer;
        this.model = model;
        this.transmission = transmission;
        this.category = category;
        this.location = location;
    }
    public Car(){

    }


}
