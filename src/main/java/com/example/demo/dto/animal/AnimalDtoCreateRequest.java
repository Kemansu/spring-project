package com.example.demo.dto.animal;

import com.example.demo.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AnimalDtoCreateRequest {

    @NotEmpty
    @Valid
    private List<@NotNull @Min(value = 1) Long> animalTypes;

    @DecimalMin(inclusive = false, value = "0")
    private double weight;

    @DecimalMin(inclusive = false, value = "0")
    private double length;


    @DecimalMin(inclusive = false, value = "0")
    private double height;

    @NotNull
    private Gender gender;

    @Min(value = 1)
    private int chipperId;

    @Min(value = 1)
    private long chippingLocationId;


    public AnimalDtoCreateRequest() {
    }

}
