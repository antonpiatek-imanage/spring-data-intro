package com.blocks.rental.controllers;


import com.blocks.rental.dtos.*;
import com.blocks.rental.entities.*;
import com.blocks.rental.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class RentalController {

///////////////////////////////////////////////////////
///// Location Info
///////////////////////////////////////////////////////
    @Autowired LocationService locationService;


    @GetMapping(path="/locations")
    public ArrayList<LocationDto> getLocations(@RequestParam(value="page", defaultValue = "0") int page,
                                         @RequestParam(value="size", defaultValue = "25") int size,
                                         @RequestParam(value="sort", defaultValue = "id") String sort,
                                         @RequestParam(value="ascending", defaultValue = "true") boolean ascending){
        return locationService.findPageToOutput(page,size,sort,ascending);
    }

    @PostMapping(path="/location")
    public LocationDto putLocation(@RequestParam(value="country", defaultValue = "US") String country){
        Location location = locationService.add(country);
        return locationService.mapToDto(location);
    }

    @GetMapping(path="/location/{id}")
    public LocationDto getLocationById(@PathVariable(value="id") long id){

        Location location = locationService.findById(id);
        return locationService.mapToDto(location);
    }

    @PostMapping(path="location/{id}/edit")
    public LocationDto editLocation(@PathVariable(value="id") long id, @RequestParam(value="country") String country){
        Location location = locationService.edit(id, country);
        return locationService.mapToDto(location);
    }

    @PostMapping(path="location/{id}/delete")
    public LocationDto deleteLocation(@PathVariable(value="id") long id){
        Location location = locationService.delete(id);
        return locationService.mapToDto(location);
    }
///////////////////////////////////////////////////////
///// Customer Info
///////////////////////////////////////////////////////

    @Autowired CustomerService customerService;



   @GetMapping(path="/customers")
    public ArrayList<CustomerDto> getCustomers(@RequestParam(value="page", defaultValue = "0") int page,
                                         @RequestParam(value="size", defaultValue = "25") int size,
                                         @RequestParam(value="sort", defaultValue = "id") String sort,
                                         @RequestParam(value="ascending", defaultValue = "true") boolean ascending){
        return customerService.findPageToOutput(page, size, sort, ascending);
    }

    @PostMapping(path="/customer")
    public CustomerDto putCustomer(@RequestParam(value="name", defaultValue = "NONE") String name){
        Customer customer = customerService.add(name);
        return customerService.mapToDto(customer);
    }

    @GetMapping(path="/customer/{id}")
    public CustomerDto getCustomerById(@PathVariable(value="id") long id){
        Customer customer = customerService.findById(id);
        return customerService.mapToDto(customer);

    }

    @PostMapping(path="customer/{id}/edit")
    public CustomerDto editCustomer(@PathVariable(value="id") long id, @RequestParam(value="name") String name){
        Customer customer = customerService.editCustomer(id, name);
        return customerService.mapToDto(customer);

    }

    @PostMapping(path="customer/{id}/delete")
    public CustomerDto deleteCustomer(@PathVariable(value="id") long id){
        Customer customer = customerService.delete(id);
        return customerService.mapToDto(customer);

    }

///////////////////////////////////////////////////////
///// Car Info
///////////////////////////////////////////////////////

    @Autowired CarService carService;


    @GetMapping(path="/cars")
    public ArrayList<CarDto> getCars(@RequestParam(value="page", defaultValue = "0") int page,
                                         @RequestParam(value="size", defaultValue = "25") int size,
                                         @RequestParam(value="sort", defaultValue = "id") String sort,
                                         @RequestParam(value="ascending", defaultValue = "true") boolean ascending){
        return carService.findPageToOutput(page, size, sort, ascending);
    }

    @PostMapping(path="/car")
    public CarDto putCar(
            @RequestParam(value="name") String name,
            @RequestParam(value="registration") String registration,
            @RequestParam(value="manufacturer") String manufacturer,
            @RequestParam(value="model") String model,
            @RequestParam(value="transmission") Car.Transmission transmission,
            @RequestParam(value="category") Car.Category category,
            @RequestParam(value="locationID") long locationID){
        Car car = carService.handleCar(name, registration, manufacturer, model, transmission, category, locationID);
        return carService.mapToDto(car);
    }

    @GetMapping(path="/car/{id}")
    public CarDto getCarById(@PathVariable(value="id") long id){

        Car car = carService.findById(id);
        return carService.mapToDto(car);
    }

    @PostMapping(path="/car/{id}/edit")
    public CarDto editCar(@PathVariable(value="id") long id,
                       @RequestParam(value="name", defaultValue = "null") String name,
                       @RequestParam(value="manufacturer", defaultValue="null") String manufacturer,
                       @RequestParam(value="model", defaultValue="null") String model,
                       @RequestParam(value="transmission", defaultValue="none") Car.Transmission transmission,
                       @RequestParam(value="category", defaultValue="none") Car.Category category){

        Car car = carService.editCar(id, name, manufacturer, model, transmission, category);
        return carService.mapToDto(car);
    }

    @PostMapping(path = "/car/{id}/delete")
    public CarDto deleteCar(@PathVariable(value="id") long id){
        return carService.mapToDto(carService.delete(id));
    }

///////////////////////////////////////////////////////
///// Booking Info
///////////////////////////////////////////////////////

    @Autowired BookingService bookingService;


    @GetMapping(path="/bookings")
    public ArrayList<BookingDto> getBookings(@RequestParam(value="page", defaultValue = "0") int page,
                                      @RequestParam(value="size", defaultValue = "25") int size,
                                      @RequestParam(value="sort", defaultValue = "id") String sort,
                                      @RequestParam(value="ascending", defaultValue = "true") boolean ascending){
        return bookingService.findPageToOutput(page, size, sort, ascending);
    }

    @PostMapping(path = "/booking")
    public BookingDto createBooking(@RequestParam(value = "car_id") long car_id,
                                    @RequestParam(value="customer_id") long customer_id,
                                    @RequestParam(value="startDate") Date startDate,
                                    @RequestParam(value = "endDate") Date endDate){

        Car car_link = carService.findById(car_id);
        Location location_link = car_link.location;
        Customer customer_link = customerService.findById(customer_id);
        Booking booking = bookingService.add(startDate, endDate, car_link, location_link, customer_link);
        return bookingService.mapToDto(booking);
    }

    @GetMapping(path="/booking/{id}")
    public BookingDto getBooking(@PathVariable(value = "id") long id){
        Booking booking = bookingService.findById(id);
        return bookingService.mapToDto(booking);
    }

    @PostMapping(path="/booking/{id}/edit")
    public BookingDto editBooking(@PathVariable(value = "id") long id,
                                  @RequestParam(value = "car_id", defaultValue = "-1") long car_id,
                                  @RequestParam(value = "customer_id", defaultValue = "-1") long customer_id,
                                  @RequestParam(value="startDate", defaultValue = "1970/01/01") Date startDate,
                                  @RequestParam(value = "endDate", defaultValue = "1970/01/01") Date endDate){

        Car car = car_id > 0 ? carService.findById(car_id) : null;
        Customer customer = customer_id > 0 ? customerService.findById(customer_id) : null;
        Location location = car != null ? car.location : null;
        Booking booking = bookingService.edit(id, car, location, customer, startDate,endDate);
        return bookingService.mapToDto(booking);
    }

    @PostMapping(path="booking/{id}/delete")
    public BookingDto deleteBooking(@PathVariable(value="id") long id){
        Booking booking = bookingService.delete(id);
        return bookingService.mapToDto(booking);
    }




///////////////////////////////////////////////////////
///// Additional REST Endpoints
///////////////////////////////////////////////////////


    @GetMapping(path="/location/{id}/cars")
    public Set<CarDto> getLocationCars(@PathVariable(value="id") long id){
        Location location = locationService.findById(id);
        if (location == null)
            return null;
        Set<Car> cars = location.getCarsInLocation();
        Set<CarDto> carsDto = new HashSet<>();
        for (Car car : cars){
            carsDto.add(carService.mapToDto(car));
        }
        return carsDto;
    }

    @GetMapping(path="/locations/summary")
    public List<LocationSummary> getLocationSummary(){

        Iterable<Location> locationIterable = locationService.findAllLocations();
        ArrayList<LocationSummary> locations = new ArrayList<>();
        for ( Location l : locationIterable){
            locations.add(new LocationSummary(l.id, l.country, l.getCarsInLocation().size(),
                    l.getBookingsInLocation().size(), l.getActiveBookingsInLocation().size()));

        }
        return locations;
    }

    @GetMapping(path="location/{id}/detailed")
    public LocationDetailedDto getDetailedDto(@PathVariable(value="id") long id){
        Location location = locationService.findById(id);
        return locationService.mapToDetailedDto(location);
    }


}
