package com.example.demo.controllers;

import com.example.demo.entities.LocationEntity;
import com.example.demo.model.dto.LocationCreationDTO;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping(path = "/createLocation")
    public LocationEntity createLocation(@RequestBody LocationCreationDTO locationDTO)
    {
        //include validation possibly.
        LocationEntity location = locationDTO.convertDtoToEntity();
        return locationService.createLocation(location);
    }

    @ResponseBody
    @GetMapping(path = "/findLocation")
    public LocationEntity findById(@RequestParam("id") Long id)
    {
    return locationService.findLocationById(id);
    }

    @ResponseBody
    @GetMapping(path = "/allLocations")
    public List<LocationEntity> allLocations()
    {
        return locationService.findAllLocations();
    }

    @ResponseBody
    @GetMapping(path = "/allLocationsOrdered")
    public List<LocationEntity> allLocationsOrdered()
    {
        return locationService.findAllLocationsAscending();
    }

    @PutMapping(path = "/updateLocation/{id}")
    public ResponseEntity<LocationEntity> updateLocation(@RequestBody LocationEntity location, @PathVariable Long id)
    {
        return ResponseEntity.ok(locationService.updateLocation(location, id));
    }

    @DeleteMapping(path = "/deleteLocation/{id}")
    public void deleteLocation(@PathVariable Long id)
    {
        locationService.deleteLocation(id);
    }
}
