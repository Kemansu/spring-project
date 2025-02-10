package com.example.demo.dto.animal;

import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Setter
@Getter
public class AnimalDtoResponse {
    private Long id;

    private List<Long> animalTypes;

    private Double weight;

    private Double length;

    private Double height;

    private Gender gender;

    private LifeStatus lifeStatus;

    private String chippingDateTime;

    private Integer chipperId;

    private Long chippingLocationId;

    private List<Long> visitedLocations;

    private String deathDateTime;

}
