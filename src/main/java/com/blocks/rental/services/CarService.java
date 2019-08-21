package com.blocks.rental.services;

import com.blocks.rental.entities.Car;
import com.blocks.rental.entities.Location;
import com.blocks.rental.repositories.CarRepository;
import com.blocks.rental.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {


    @Autowired
    CarRepository carRepository;

    @Autowired
    LocationRepository locationRepository;

    public Car findById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public void save(Car customer){
        carRepository.save(customer);
    }


    public Car handleCar(String name, String registration, String manufacturer, String model, String transmission,
                         String category, long locationID) {
        Location location = locationRepository.findById(locationID).get();
        Car car = new Car(name, registration, manufacturer, model, transmission, category, location);
        this.save(car);
        return car;
    }

    public Car editCar(long id, String name, String manufacturer, String model, String transmission, String category) {
        Car car = findById(id);
        if (car==null){
            return null;
        }
        if (!name.equals("null")){
            car.name = name;
        }
        if (!manufacturer.equals("null")){
            car.manufacturer = manufacturer;
        }
        if (!model.equals("null")){
            car.model = model;
        }
        if (!transmission.equals("null")){
            car.transmission = transmission;
        }
        if (!category.equals("null")){
            car.category = category;
        }
        this.save(car);
        return car;
    }

    public Car delete(long id) {
        Car car = findById(id);
        carRepository.deleteById(id);
        return car;
    }
}
