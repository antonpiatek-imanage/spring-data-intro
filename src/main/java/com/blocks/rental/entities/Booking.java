package com.blocks.rental.entities;

import javax.persistence.*;
import java.util.Date;


@Entity
public class Booking {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public long id;

    public Date startDate;
    public Date endDate;

    @ManyToOne(optional = false)
    public Car car_link;
    @ManyToOne(optional = false)
    public Location location_link;
    @ManyToOne(optional = false)
    public Customer customer_link;


    public Booking(Date startDate, Date endDate, Car car_link, Location location_link, Customer customer_link){
        this.startDate = startDate;
        this.endDate = endDate;
        this.car_link = car_link;
        this.location_link = location_link;
        this.customer_link = customer_link;
    }

    public Booking(){

    }

    @PreRemove
    private void removeFromSets(){
        car_link.bookings.remove(this);
        customer_link.bookings.remove(this);
        location_link.getBookingsInLocation().remove(this);
    }

}



