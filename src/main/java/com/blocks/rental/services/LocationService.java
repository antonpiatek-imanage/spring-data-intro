package com.blocks.rental.services;

import com.blocks.rental.dtos.LocationDetailedDto;
import com.blocks.rental.dtos.LocationDto;
import com.blocks.rental.dtos.SortOrderEnum;
import com.blocks.rental.entities.Booking;
import com.blocks.rental.entities.Location;
import com.blocks.rental.pages.RentalPageable;
import com.blocks.rental.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.blocks.rental.mapper.LocationMapper;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LocationService {


    @Autowired
    LocationRepository locationRepository;

    public Location findById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    private void save(Location location){
        locationRepository.save(location);
    }

    public Location delete(long id){
        Location loc = findById(id);
        locationRepository.deleteById(id);
        return loc;
    }

    public Location edit(long id, String country) {
        Location loc = findById(id);
        loc.country = country;

        this.save(loc);
        return loc;
    }



    public Location add(String country) {
        Location loc = new Location(country);
        this.save(loc);
        return loc;
    }

    public Iterable<Location> findAllLocations(){
        return locationRepository.findAll();
    }

    public LocationDto mapToDto(Location location) {
        return LocationMapper.INSTANCE.locationToLocationDto(location);
    }

    public LocationDetailedDto mapToDetailedDto(Location location){

        return LocationMapper.INSTANCE.locationToLocationDetailedDto(location);
    }


    public ArrayList<LocationDto> findPageToOutput(int page, int size, String sort, boolean ascending) {
        RentalPageable pageable = new RentalPageable(page, size, sort, ascending);
        Page pageReturned = locationRepository.findAll(pageable);
        Stream<Location> stream = pageReturned.get();
        ArrayList<LocationDto> arrayList = new ArrayList<>();
        stream.forEach(location -> arrayList.add(mapToDto(location)));
        return arrayList;
    }
}
