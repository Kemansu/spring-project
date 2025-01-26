package com.example.demo.dto.animal;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalDtoUpdateTypeRequest {

    @Min(value = 1)
    private long oldTypeId;

    @Min(value = 1)
    private long newTypeId;

    public AnimalDtoUpdateTypeRequest() {
    }

}
