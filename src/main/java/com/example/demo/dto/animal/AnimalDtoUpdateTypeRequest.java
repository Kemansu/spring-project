package com.example.demo.dto.animal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalDtoUpdateTypeRequest {

    @NotNull
    @Min(value = 1)
    private Long oldTypeId;

    @NotNull
    @Min(value = 1)
    private Long newTypeId;

}
