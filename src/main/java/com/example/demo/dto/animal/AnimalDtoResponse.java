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
    private long id;

    private List<Long> animalTypes;

    private double weight;

    private double length;

    private double height;

    private Gender gender;

    private LifeStatus lifeStatus;

    private String chippingDateTime;

    private int chipperId;

    private long chippingLocationId;

    private List<Long> visitedLocations;

    private String deathDateTime;

    public AnimalDtoResponse() {
    }


}
