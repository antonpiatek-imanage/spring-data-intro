package com.blocks.rental.controllers;


import com.blocks.rental.entities.*;
import com.blocks.rental.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class RentalController {

///////////////////////////////////////////////////////
///// Location Info
///////////////////////////////////////////////////////
    @Autowired LocationService locationService;

    @PostMapping(path="/location")
    public Location putLocation(@RequestParam(value="country", defaultValue = "US") String country){
        return locationService.add(country);

    }

    @GetMapping(path="/location")
    public Location getLocationById(@RequestParam(value="id", defaultValue = "0") long id){
        return locationService.findById(id);
    }

    @PostMapping(path="location/edit")
    public Location editLocation(@RequestParam(value="id") long id, @RequestParam(value="country") String country){
        return locationService.edit(id, country);

    }
    @PostMapping(path="location/delete")
    public Location deleteLocation(@RequestParam(value="id") long id){
        return locationService.delete(id);

    }
///////////////////////////////////////////////////////
///// Customer Info
///////////////////////////////////////////////////////

    @Autowired CustomerService customerService;

    @PostMapping(path="/customer")
    public Customer putCustomer(@RequestParam(value="name", defaultValue = "NONE") String name){
        return customerService.add(name);

    }
    @GetMapping(path="/customer")
    public Customer getCustomerById(@RequestParam(value="id", defaultValue = "0") long id){
        return customerService.findById(id);
    }

    @PostMapping(path="customer/edit")
    public Customer editCustomer(@RequestParam(value="id") long id, @RequestParam(value="name") String name){
        return customerService.editCustomer(id, name);

    }
    @PostMapping(path="customer/delete")
    public Customer deleteCustomer(@RequestParam(value="id") long id){
        return customerService.delete(id);

    }
///////////////////////////////////////////////////////
///// Car Info
///////////////////////////////////////////////////////

    @Autowired CarService carService;

    @PostMapping(path="/car")
    public Car putCar(
            @RequestParam(value="name") String name,
            @RequestParam(value="registration") String registration,
            @RequestParam(value="manufacturer") String manufacturer,
            @RequestParam(value="model") String model,
            @RequestParam(value="transmission") String transmission,
            @RequestParam(value="category") String category,
            @RequestParam(value="locationID") long locationID){
        return carService.handleCar(name, registration, manufacturer, model, transmission, category, locationID);

    }

    @GetMapping(path="/car")
    public Car getCarById(@RequestParam(value="id", defaultValue = "0") long id){
        return carService.findById(id);
    }

    @PostMapping(path="/car/edit")
    public Car editCar(@RequestParam(value="id") long id,
                       @RequestParam(value="name", defaultValue = "null") String name,
                       @RequestParam(value="manufacturer", defaultValue="null") String manufacturer,
                       @RequestParam(value="model", defaultValue="null") String model,
                       @RequestParam(value="transmission", defaultValue="null") String transmission,
                       @RequestParam(value="category", defaultValue="null") String category){

        return carService.editCar(id, name, manufacturer, model, transmission, category);

    }

    @PostMapping(path = "/car/delete")
    public Car deleteCar(@RequestParam(value="id") long id){
        return carService.delete(id);

    }

///////////////////////////////////////////////////////
///// Booking Info
///////////////////////////////////////////////////////

    @Autowired BookingService bookingService;

    @PostMapping(path = "/booking")
    public Booking createBooking(@RequestParam(value = "car_id") long car_id,
                                    @RequestParam(value="location_id") long location_id,
                                    @RequestParam(value="customer_id") long customer_id,
                                    @RequestParam(value="startDate") Date startDate,
                                    @RequestParam(value = "endDate") Date endDate){

        Car car_link = carService.findById(car_id);
        Location location_link = locationService.findById(location_id);
        Customer customer_link = customerService.findById(customer_id);
        return bookingService.add(startDate, endDate, car_link, location_link, customer_link);
    }
}
