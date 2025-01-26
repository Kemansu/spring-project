package com.example.demo.dto.animal;

import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalDtoUpdateRequest {
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

    @NotNull
    private LifeStatus lifeStatus;

    public AnimalDtoUpdateRequest() {
    }

}
