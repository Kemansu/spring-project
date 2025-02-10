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

    @NotNull
    @DecimalMin(inclusive = false, value = "0")
    private Double weight;

    @NotNull
    @DecimalMin(inclusive = false, value = "0")
    private Double length;

    @NotNull
    @DecimalMin(inclusive = false, value = "0")
    private Double height;

    @NotNull
    private Gender gender;

    @NotNull
    @Min(value = 1)
    private Integer chipperId;

    @NotNull
    @Min(value = 1)
    private Long chippingLocationId;

    @NotNull
    private LifeStatus lifeStatus;

}
