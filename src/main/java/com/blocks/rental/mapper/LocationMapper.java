package com.blocks.rental.mapper;

import com.blocks.rental.dtos.LocationDetailedDto;
import com.blocks.rental.dtos.LocationDto;
import com.blocks.rental.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationDetailedDto locationToLocationDetailedDto(Location location);

    LocationDto locationToLocationDto(Location location);


}
