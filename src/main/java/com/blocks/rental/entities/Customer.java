package com.blocks.rental.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer_link", fetch = FetchType.EAGER)
    Set<Booking> bookings;

    public String name;


    public Customer(String name){
        this.name = name;
    }
    public Customer(){

    }
}
