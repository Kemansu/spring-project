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

}
