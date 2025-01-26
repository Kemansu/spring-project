package com.example.demo.model;

import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "animal")
public class Animal {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    @JoinTable(
            name = "animal_types",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_type_id")
    )
    private List<AnimalType> animalTypes;

    @Column(name = "weight")
    private double weight;

    @Column(name = "length")
    private double length;

    @Column(name = "height")
    private double height;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "life_status")
    @Enumerated(EnumType.STRING)
    private LifeStatus lifeStatus;

    @Column(name = "chipping_date_time")
    private String chippingDateTime;

    @ManyToOne
    @JoinColumn(name = "chipper_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "chipping_location_id")
    private Location location;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimalVisitedLocations> visitedLocations = new ArrayList<>();


    @Column(name = "death_date_time")
    private String deathDateTime;

    public Animal() {
    }


    public void addAnimalType(AnimalType animalType) {
        animalTypes.add(animalType);
    }

    public void removeAnimalType(AnimalType animalType) {
        animalTypes.remove(animalType);
    }

    public void addVisitedLocation(Location location) {
        AnimalVisitedLocations visitedLocation = new AnimalVisitedLocations();
        visitedLocation.setAnimal(this);
        visitedLocation.setLocation(location);
        visitedLocation.setDateTimeOfVisitLocationPoint(String.valueOf(Instant.now()));
        visitedLocations.add(visitedLocation);
    }

    public void removeVisitedLocation(AnimalVisitedLocations visitedLocation) {
        visitedLocations.remove(visitedLocation);
    }

    public void removeVisitedLocation(int index) {
        visitedLocations.remove(index);
    }
}