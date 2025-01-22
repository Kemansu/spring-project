package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String gender;

    @Column(name = "life_status")
    private String lifeStatus;

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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addAnimalType(AnimalType animalType) {
        animalTypes.add(animalType);
    }

    public void removeAnimalType(AnimalType animalType) {
        animalTypes.remove(animalType);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(String lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public String getChippingDateTime() {
        return chippingDateTime;
    }

    public void setChippingDateTime(String chippingDateTime) {
        this.chippingDateTime = chippingDateTime;
    }

    public List<AnimalType> getAnimalTypes() {
        return animalTypes;
    }

    public void setAnimalTypes(List<AnimalType> animalTypes) {
        this.animalTypes = animalTypes;
    }

    public List<AnimalVisitedLocations> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocation(List<AnimalVisitedLocations> visitedLocation) {
        this.visitedLocations = visitedLocation;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDeathDateTime() {
        return deathDateTime;
    }

    public void setDeathDateTime(String deathDateTime) {
        this.deathDateTime = deathDateTime;
    }

    public void setAccountId(int id) {
        account.setId(id);
    }

    public void setLocationId(long id) {
        location.setId(id);
    }

    public AnimalVisitedLocations addVisitedLocation(Location location) {
        AnimalVisitedLocations visitedLocation = new AnimalVisitedLocations();
        visitedLocation.setAnimal(this);
        visitedLocation.setLocation(location);
        visitedLocation.setDateTimeOfVisitLocationPoint(String.valueOf(Instant.now()));
        visitedLocations.add(visitedLocation);
        return visitedLocation;
    }

    public void removeVisitedLocation(AnimalVisitedLocations visitedLocation) {
        visitedLocations.remove(visitedLocation);
    }

    public void removeVisitedLocation(int index) {
        visitedLocations.remove(index);
    }
}