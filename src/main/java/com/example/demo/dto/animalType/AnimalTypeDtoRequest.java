package com.example.demo.dto.animalType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalTypeDtoRequest {

    @NotNull
    @NotBlank
    private String type;

}
