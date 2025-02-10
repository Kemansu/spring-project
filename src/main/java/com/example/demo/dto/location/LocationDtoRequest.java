package com.example.demo.dto.location;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDtoRequest {


    @NotNull()
    @Min(value = -90)
    @Max(value = 90)
    private Double latitude;

    @NotNull()
    @Min(value = -180)
    @Max(value = 180)
    private Double longitude;

}
