package com.blocks.rental.services;

import com.blocks.rental.entities.Location;
import com.blocks.rental.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {


    @Autowired
    LocationRepository locationRepository;

    public Location findById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public void save(Location location){
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
}
