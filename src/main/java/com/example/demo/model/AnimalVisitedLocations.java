package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@Table(name = "animal_visited_locations")
public class AnimalVisitedLocations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "date_time_of_visit_location_point", nullable = false)
    private String dateTimeOfVisitLocationPoint;

    public AnimalVisitedLocations() {
    }
}
