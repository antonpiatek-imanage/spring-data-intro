package com.blocks.rental.services;

import com.blocks.rental.dtos.CarDto;
import com.blocks.rental.dtos.CustomerDto;
import com.blocks.rental.dtos.LocationDto;
import com.blocks.rental.entities.Car;
import com.blocks.rental.entities.Customer;
import com.blocks.rental.entities.Location;
import com.blocks.rental.mapper.CarMapper;
import com.blocks.rental.pages.RentalPageable;
import com.blocks.rental.repositories.CarRepository;
import com.blocks.rental.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.blocks.rental.entities.Car.Category;
import com.blocks.rental.entities.Car.Transmission;

import java.util.ArrayList;
import java.util.stream.Stream;

@Service
public class CarService {


    @Autowired CarRepository carRepository;

    @Autowired LocationRepository locationRepository;

    public Car findById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    private void save(Car customer){
        carRepository.save(customer);
    }


    public Car handleCar(String name, String registration, String manufacturer, String model,
                         Car.Transmission transmission, Car.Category category, long locationID) {
        Location location = locationRepository.findById(locationID).get();
        Car car = new Car(name, registration, manufacturer, model, transmission, category, location);
        this.save(car);
        return car;
    }

    public Car editCar(long id, String name, String manufacturer, String model,
                       Transmission transmission, Category category) {
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
        if (transmission != Transmission.none){
            car.transmission = transmission;
        }
        if (category != Category.none){
            car.category = category;
        }
        this.save(car);
        return car;
    }

    public Car delete(long id) {
        Car car = this.findById(id);
        carRepository.delete(car);
        return car;
    }

    public CarDto mapToDto(Car car) {
        return CarMapper.INSTANCE.carToCarDto(car);
    }

    public ArrayList<CarDto> findPageToOutput(int page, int size, String sort, boolean ascending) {
        RentalPageable pageable = new RentalPageable(page, size, sort, ascending);
        Page pageReturned = carRepository.findAll(pageable);
        Stream<Car> stream = pageReturned.get();
        ArrayList<CarDto> arrayList = new ArrayList<>();
        stream.forEach(location -> arrayList.add(mapToDto(location)));

        return arrayList;
    }
}
