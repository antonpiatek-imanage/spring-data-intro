package com.blocks.rental.mapper;

import com.blocks.rental.dtos.CarDto;
import com.blocks.rental.entities.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDto carToCarDto(Car car);

}
