package com.example.demo.mapper;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDtoResponse toLocationDtoResponse(Location location);

    @Mapping(target = "id", expression = "java(0)")
    Location toLocation(LocationDtoRequest request);

}
