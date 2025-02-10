package com.example.demo.dto.animal;

import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimalDtoSearchRequest {
    private String startDateTime;
    private String endDateTime;
    private Long chipperId;
    private Long chippingLocationId;
    private LifeStatus lifeStatus;
    private Gender gender;
    private Integer from;
    private Integer size;
}
