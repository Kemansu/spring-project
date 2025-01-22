package com.example.demo.model;

import jakarta.persistence.*;


import java.time.ZonedDateTime;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDateTimeOfVisitLocationPoint() {
        return dateTimeOfVisitLocationPoint;
    }

    public void setDateTimeOfVisitLocationPoint(String dateTimeOfVisitLocationPoint) {
        this.dateTimeOfVisitLocationPoint = dateTimeOfVisitLocationPoint;
    }
}
